/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.web.zrna;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.io.StringReader;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.WebServiceRef;
import nwtis.dvertovs.ejb.eb.DvertovsUsers;
import nwtis.dvertovs.rest.klijent.MeteoRESTClient;
import nwtis.dvertovs.web.soap.klijent.Adresa;
import nwtis.dvertovs.web.soap.klijent.DvertovsAplikacija1WS_Service;
import nwtis.dvertovs.web.soap.klijent.MeteoPodaci;
import org.foi.nwtis.dvertovs.konfiguracije.Konfiguracija;

/**
 *
 * @author dare
 */
@Named(value = "korisnikBean")
@SessionScoped
public class KorisnikBean implements Serializable {

    @WebServiceRef(wsdlLocation = "WEB-INF/wsdl/localhost_8080/dvertovs_aplikacija_1/DvertovsAplikacija1WS.wsdl")
    private DvertovsAplikacija1WS_Service service;

    private String novaAdresa;
    private String odabranaAdresa;

    private Map<String, nwtis.dvertovs.web.soap.klijent.Adresa> popisAdresa;

    private String responseServer;

    private List<String> popisDodanihAdresa;

    private MeteoPodaci zadnjiMeteoPodaci;

    private List<MeteoPodaci> listaMeteoPrognoza;

    private String prikazMape;
    private String prikazPrognoza;

    /**
     * Creates a new instance of KorisnikBean
     */
    public KorisnikBean() {
        prikazPrognoza = "hidden";
        prikazMape = "hidden";
    }

    public void dodajNovuAdresu() {
        this.responseServer = posaljiZahtjevNaServer("ADD " + this.novaAdresa);
    }

    private DvertovsUsers getUserInfo() {
        FacesContext context = FacesContext.getCurrentInstance();
        return (DvertovsUsers) context.getExternalContext().getSessionMap().get("user");
    }

    public void preuzmiMeteo() {
        this.prikazMape = "visible";

    }

    public void sakrijMeteo() {
        this.prikazMape = "hidden";
    }

    public void togglePrognoza() {
        this.prikazPrognoza = (this.prikazPrognoza.equals("visible") ? "hidden" : "visible");
    }

    public void fetchAllAddresses() {
        MeteoRESTClient mrestc = new MeteoRESTClient();
        String odgovor = mrestc.getJsonAddresses(getUserInfo().getUsername(), getUserInfo().getPassword());
        JsonReader reader = Json.createReader(new StringReader(odgovor));
        JsonObject jo = reader.readObject();

        JsonArray meteoDataArray = jo.getJsonArray("adrese");
        this.popisDodanihAdresa = new ArrayList<>();
        for (int i = 0; i < meteoDataArray.size(); i++) {
            popisDodanihAdresa.add(meteoDataArray.getString(i));
        }
    }

    public void fetchMyAddresses() {
        List<nwtis.dvertovs.web.soap.klijent.Adresa> adrese = dajSveAdrese("USER " + getUserInfo().getUsername() + "; PASSWD " + getUserInfo().getUsername() + ";");
        this.popisDodanihAdresa = new ArrayList<>();
        this.popisAdresa = new HashMap<>();
        for (nwtis.dvertovs.web.soap.klijent.Adresa a : adrese) {
            this.popisDodanihAdresa.add(a.getAdresa());
            this.popisAdresa.put(a.getAdresa(), a);
        }
    }

    private String posaljiZahtjevNaServer(String command) {
        FacesContext context = FacesContext.getCurrentInstance();
        ServletContext servletContext = (ServletContext) context.getExternalContext().getContext();
        DvertovsUsers dv = getUserInfo();
        Konfiguracija konfig = (Konfiguracija) servletContext.getAttribute("App_Konfig");

        StringBuilder response = null;
        try {
            Socket socket = new Socket(konfig.dajPostavku("primitivniPosluziteljAdresa"), Integer.parseInt(konfig.dajPostavku("portPrimitivnogPosluzitelja")));
            OutputStream os = socket.getOutputStream();
            InputStream is = socket.getInputStream();

            String request = "USER " + dv.getUsername() + "; PASSWD " + dv.getPassword() + "; " + command + ";";
            os.write(request.getBytes());
            os.flush();
            socket.shutdownOutput();

            response = new StringBuilder();
            int bajt;
            while ((bajt = is.read()) != -1) {
                response.append((char) bajt);
            }
            is.close();
            os.close();
            System.out.println("RESPONSE: " + response);
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(AdminBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response.toString();
    }

    private java.util.List<nwtis.dvertovs.web.soap.klijent.Adresa> dajSveAdrese(java.lang.String auth) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        nwtis.dvertovs.web.soap.klijent.DvertovsAplikacija1WS port = service.getDvertovsAplikacija1WSPort();
        return port.dajSveAdrese(auth);
    }

    private MeteoPodaci dajZadnjeMeteoPodatke(java.lang.String auth, java.lang.String adresa) {
        // Note that the injected javax.xml.ws.Service reference as well as port objects are not thread safe.
        // If the calling of port operations may lead to race condition some synchronization is required.
        nwtis.dvertovs.web.soap.klijent.DvertovsAplikacija1WS port = service.getDvertovsAplikacija1WSPort();
        return port.dajZadnjeMeteoPodatke(auth, adresa);
    }

    public List<String> getPopisDodanihAdresa() {
        if (novaAdresa != null) {
            fetchAllAddresses();
        }
        novaAdresa = null;
        return popisDodanihAdresa;
    }

    public MeteoPodaci getZadnjiMeteoPodaci() {
        if (this.odabranaAdresa != null) {
            this.zadnjiMeteoPodaci = dajZadnjeMeteoPodatke("USER " + getUserInfo().getUsername() + "; PASSWD " + getUserInfo().getUsername() + ";", this.odabranaAdresa);
        }
        return zadnjiMeteoPodaci;
    }

    public List<MeteoPodaci> getListaMeteoPrognoza() {
        if (this.odabranaAdresa != null) {
            MeteoRESTClient mrestc = new MeteoRESTClient();
            String odgovor = mrestc.getJson(getUserInfo().getUsername(), getUserInfo().getPassword(), Long.toString(this.popisAdresa.get(this.odabranaAdresa).getIdadresa()));
            JsonReader reader = Json.createReader(new StringReader(odgovor));
            JsonObject jo = reader.readObject();

            JsonArray meteoDataArray = jo.getJsonArray("forecast");
            this.listaMeteoPrognoza = new ArrayList<>();
            for (int i = 0; i < meteoDataArray.size(); i++) {
                MeteoPodaci mp = setMeteoObject(
                        meteoDataArray.getJsonObject(i).getString("vrijemeopis"),
                        meteoDataArray.getJsonObject(i).getString("temp"),
                        meteoDataArray.getJsonObject(i).getString("vlaga"),
                        meteoDataArray.getJsonObject(i).getString("tlak"),
                        meteoDataArray.getJsonObject(i).getString("vrijemeprognoze")
                );
                listaMeteoPrognoza.add(mp);
            }
        }
        return listaMeteoPrognoza;
    }

    private MeteoPodaci setMeteoObject(String vrijemeopis, String temp, String vlaga, String tlak, String vrijemeprognoze) {
        MeteoPodaci mp = new MeteoPodaci();
        mp.setWeatherValue(vrijemeopis);
        mp.setTemperatureValue(Float.parseFloat(temp));
        mp.setHumidityValue(Float.parseFloat(vlaga));
        mp.setPressureValue(Float.parseFloat(tlak));
        Date dob = null;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        try {
            dob = df.parse(vrijemeprognoze);
        } catch (ParseException ex) {
            Logger.getLogger(KorisnikBean.class.getName()).log(Level.SEVERE, null, ex);
        }
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(dob);
        try {
            XMLGregorianCalendar xmlDate = DatatypeFactory.newInstance().newXMLGregorianCalendar(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH), dob.getHours(), dob.getMinutes(), dob.getSeconds(), DatatypeConstants.FIELD_UNDEFINED, cal.getTimeZone().LONG).normalize();
            mp.setLastUpdate(xmlDate);
        } catch (DatatypeConfigurationException ex) {
            Logger.getLogger(KorisnikBean.class.getName()).log(Level.SEVERE, null, ex);
        }

        return mp;
    }

    public Map<String, Adresa> getPopisAdresa() {
        List<nwtis.dvertovs.web.soap.klijent.Adresa> adrese = dajSveAdrese("USER " + getUserInfo().getUsername() + "; PASSWD " + getUserInfo().getUsername() + ";");
        this.popisAdresa = new HashMap<>();
        for (nwtis.dvertovs.web.soap.klijent.Adresa a : adrese) {
            this.popisAdresa.put(a.getAdresa(), a);
        }
        return popisAdresa;
    }

    public void setPopisAdresa(Map<String, Adresa> popisAdresa) {
        this.popisAdresa = popisAdresa;
    }

    public void setListaMeteoPrognoza(List<MeteoPodaci> listaMeteoPrognoza) {
        this.listaMeteoPrognoza = listaMeteoPrognoza;
    }

    public void setZadnjiMeteoPodaci(MeteoPodaci zadnjiMeteoPodaci) {
        this.zadnjiMeteoPodaci = zadnjiMeteoPodaci;
    }

    public void setPopisDodanihAdresa(List<String> popisDodanihAdresa) {
        this.popisDodanihAdresa = popisDodanihAdresa;
    }

    public String getNovaAdresa() {
        return novaAdresa;
    }

    public void setNovaAdresa(String novaAdresa) {
        this.novaAdresa = novaAdresa;
    }

    public String getOdabranaAdresa() {
        return odabranaAdresa;
    }

    public void setOdabranaAdresa(String odabranaAdresa) {
        this.odabranaAdresa = odabranaAdresa;
    }

    public String getResponseServer() {
        return responseServer;
    }

    public void setResponseServer(String responseServer) {
        this.responseServer = responseServer;
    }

    public String getPrikazMape() {
        return prikazMape;
    }

    public void setPrikazMape(String prikazMape) {
        this.prikazMape = prikazMape;
    }

    public String getPrikazPrognoza() {
        return prikazPrognoza;
    }

    public void setPrikazPrognoza(String prikazPrognoza) {
        this.prikazPrognoza = prikazPrognoza;
    }

}
