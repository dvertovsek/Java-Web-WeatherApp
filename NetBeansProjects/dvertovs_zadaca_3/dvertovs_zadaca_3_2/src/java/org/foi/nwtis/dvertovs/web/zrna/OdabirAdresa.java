/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.web.zrna;

import java.io.File;
import java.io.Serializable;
import java.io.StringReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletContext;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import org.foi.nwtis.dvertovs.konfiguracije.Konfiguracija;
import org.foi.nwtis.dvertovs.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.dvertovs.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.dvertovs.ws.klijenti.MeteoWSKlijent;
import org.foi.nwtis.dvertovs.ws.klijenti.RESTClient;
import org.foi.nwtis.dvertovs.ws.serveri.Adresa;
import org.foi.nwtis.dvertovs.ws.serveri.MeteoPodaci;

/**
 *
 * @author dare
 */
@Named(value = "odabirAdresa")
@SessionScoped
public class OdabirAdresa implements Serializable {
    
    private Map<String, List<MeteoPodaci>> adreseMeteo;
    private List<String> odabraneAdrese;
    
    private Map<String, String> listaAdresa = new LinkedHashMap<>();
    private final Map<String, Adresa> idAdreseNaziv = new LinkedHashMap<>();

    /**
     * Creates a new instance of OdabirAdresa
     */
    public OdabirAdresa() {
    }
    
    public Map<String, Adresa> getIdAdreseNaziv() {
        return idAdreseNaziv;
    }
    
    public Map<String, List<MeteoPodaci>> getAdreseMeteo() {
        return adreseMeteo;
    }
    
    public void setAdreseMeteo(Map<String, List<MeteoPodaci>> adreseMeteo) {
        this.adreseMeteo = adreseMeteo;
    }
    
    public List<String> getOdabraneAdrese() {
        return odabraneAdrese;
    }
    
    public void setOdabraneAdrese(List<String> odabraneAdrese) {
        this.odabraneAdrese = odabraneAdrese;
    }
    
    public Map<String, String> getListaAdresa() {
        List<Adresa> adrese = MeteoWSKlijent.dajSveAdrese();
        listaAdresa = new LinkedHashMap<>();
        adrese.stream().forEach((a) -> {
            listaAdresa.put(a.getAdresa(), Long.toString(a.getIdadresa()));
            idAdreseNaziv.put(Long.toString(a.getIdadresa()), a);
        });
        return listaAdresa;
    }
    
    public void setListaAdresa(Map<String, String> listaAdresa) {
        this.listaAdresa = listaAdresa;
    }
    
    public void chooseAddress() {
        this.adreseMeteo = null;
    }
    
    public void callRESTOWMForecast() {
        this.adreseMeteo = new LinkedHashMap<>();
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String konfiguracija = servletContext.getInitParameter("konfiguracijaApp");
        String putanje = servletContext.getRealPath("/WEB-INF") + File.separator;
        
        Konfiguracija konfig = null;
        try {
            konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(putanje + konfiguracija);
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(OdabirAdresa.class.getName()).log(Level.SEVERE, null, ex);
        }
        RESTClient client = new RESTClient();
        client.setTarget("http://api.openweathermap.org/data/2.5/", "forecast");
        client.setParam("lat", this.idAdreseNaziv.get(this.odabraneAdrese.get(0)).getGeoloc().getLatitude());
        client.setParam("lon", this.idAdreseNaziv.get(this.odabraneAdrese.get(0)).getGeoloc().getLongitude());
        client.setParam("appid", konfig.dajPostavku("OWMAppID"));
        String odgovor = client.getJson();
        client.close();
        JsonReader reader = Json.createReader(new StringReader(odgovor));
        JsonObject jo = reader.readObject();
        
        JsonArray meteoDataArray = jo.getJsonArray("list");
        List<MeteoPodaci> meteoPodaciLista = new ArrayList<>();
        for (int i = 0; i < meteoDataArray.size(); i++) {
            JsonObject mainObj = meteoDataArray.getJsonObject(i).getJsonObject("main");
            JsonObject windObj = meteoDataArray.getJsonObject(i).getJsonObject("wind");
            JsonArray weatherObj = meteoDataArray.getJsonObject(i).getJsonArray("weather");
            MeteoPodaci mp = setMeteoObject(
                    weatherObj.getJsonObject(0).getInt("id"),
                    weatherObj.getJsonObject(0).getString("description"),
                    new Double(mainObj.getJsonNumber("temp").doubleValue()).floatValue(),
                    new Double(mainObj.getJsonNumber("temp_min").doubleValue()).floatValue(),
                    new Double(mainObj.getJsonNumber("temp_max").doubleValue()).floatValue(),
                    new Double(mainObj.getJsonNumber("humidity").doubleValue()).floatValue(),
                    new Double(mainObj.getJsonNumber("pressure").doubleValue()).floatValue(),
                    new Double(windObj.getJsonNumber("speed").doubleValue()).floatValue(),
                    new Double(windObj.getJsonNumber("deg").doubleValue()).floatValue(),
                    meteoDataArray.getJsonObject(i).getString("dt_txt")
            );
            meteoPodaciLista.add(mp);
        }
        this.adreseMeteo.put("Prognoza", meteoPodaciLista);
    }
    
    public void callSOAPZadaca_3_1() {
        String nazivAdrese = this.idAdreseNaziv.get(this.odabraneAdrese.get(0)).getAdresa();
        List<MeteoPodaci> listamp = MeteoWSKlijent.dajSveMeteoPodatkeZaAdresu(nazivAdrese);
        this.adreseMeteo = new LinkedHashMap<>();
        this.adreseMeteo.put(nazivAdrese, listamp);
    }
    
    public void callRESTZadaca_3_1() {
        this.adreseMeteo = new LinkedHashMap<>();
        for (String a : this.odabraneAdrese) {
            RESTClient client = new RESTClient();
            client.setTarget("http://localhost:8080/dvertovs_zadaca_3_1/webresources", "meteoREST/" + a);
            String odgovor = client.getJson();
            client.close();
            
            JsonReader reader = Json.createReader(new StringReader(odgovor));
            JsonObject jo = reader.readObject();
            
            List<MeteoPodaci> meteoPodaciLista = new ArrayList<>();
            JsonArray meteoDataArray = jo.getJsonArray("meteo");
            for (int i = 0; i < meteoDataArray.size(); i++) {
                MeteoPodaci mp = setMeteoObject(
                        meteoDataArray.getJsonObject(i).getInt("vrijeme"),
                        meteoDataArray.getJsonObject(i).getString("vrijemeopis"),
                        new Double(meteoDataArray.getJsonObject(i).getJsonNumber("temp").doubleValue()).floatValue(),
                        new Double(meteoDataArray.getJsonObject(i).getJsonNumber("tempmin").doubleValue()).floatValue(),
                        new Double(meteoDataArray.getJsonObject(i).getJsonNumber("tempmax").doubleValue()).floatValue(),
                        new Double(meteoDataArray.getJsonObject(i).getJsonNumber("vlaga").doubleValue()).floatValue(),
                        new Double(meteoDataArray.getJsonObject(i).getJsonNumber("tlak").doubleValue()).floatValue(),
                        new Double(meteoDataArray.getJsonObject(i).getJsonNumber("vjetar").doubleValue()).floatValue(),
                        new Double(meteoDataArray.getJsonObject(i).getJsonNumber("vjetarsmjer").doubleValue()).floatValue(),
                        meteoDataArray.getJsonObject(i).getString("preuzeto")
                );
                meteoPodaciLista.add(mp);
            }
            adreseMeteo.put(jo.getString("adresa"), meteoPodaciLista);
        }
    }
    
    private MeteoPodaci setMeteoObject(Integer vrijeme, String vrijemeopis, Float temp, Float tempmin, Float tempmax, Float vlaga, Float tlak, Float vjetar, Float vjetarsmjer, String preuzeto) {
        MeteoPodaci mp = new MeteoPodaci();
        mp.setWeatherNumber(vrijeme);
        mp.setWeatherValue(vrijemeopis);
        mp.setTemperatureValue(temp);
        mp.setTemperatureMin(tempmin);
        mp.setTemperatureMax(tempmax);
        mp.setHumidityValue(vlaga);
        mp.setPressureValue(tlak);
        mp.setWindSpeedValue(vjetar);
        mp.setWindDirectionValue(vjetarsmjer);
        
        Date dob = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            dob = df.parse(preuzeto);
        } catch (ParseException ex) {
            Logger.getLogger(OdabirAdresa.class.getName()).log(Level.SEVERE, null, ex);
        }
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dob);
        try {
            XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), dob.getHours(), dob.getMinutes(), dob.getSeconds(), DatatypeConstants.FIELD_UNDEFINED, cal.getTimeZone().LONG).normalize();
            mp.setLastUpdate(xmlDate);
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(OdabirAdresa.class.getName()).log(Level.SEVERE, null, ex);
        }
        return mp;
    }
}
