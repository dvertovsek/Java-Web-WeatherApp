/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.ws.serveri;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.ServletContext;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import org.foi.nwtis.dvertovs.db.DBController;
import org.foi.nwtis.dvertovs.konfiguracije.Konfiguracija;
import org.foi.nwtis.dvertovs.rest.klijenti.GMKlijent;
import org.foi.nwtis.dvertovs.rest.klijenti.OWMKlijent;
import org.foi.nwtis.dvertovs.web.podaci.Adresa;
import org.foi.nwtis.dvertovs.web.podaci.Lokacija;
import org.foi.nwtis.dvertovs.web.podaci.MeteoPodaci;

/**
 *
 * @author dare
 */
@WebService(serviceName = "GeoMeteoWS")
public class GeoMeteoWS {

    @Resource
    private WebServiceContext context;

    /**
     * Web service operation
     */
    @WebMethod(operationName = "dajSveAdrese")
    public java.util.List<Adresa> dajSveAdrese() {
        List<Adresa> listaAdresa = new ArrayList<>();
        try {
            ResultSet rs = DBController.getDbCon().getAddresses();

            while (rs.next()) {
                long addrID = rs.getLong(1);
                String addr = rs.getString(2);
                String lat = rs.getString(3);
                String lon = rs.getString(4);
                Adresa adresa = new Adresa(addrID, addr, new Lokacija(lat, lon));
                listaAdresa.add(adresa);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        return listaAdresa;
    }

    Adresa dajAdresu(long addrID, String adresa) {

        GMKlijent gmk = new GMKlijent();
        Lokacija lokacija = gmk.getGeoLocation(adresa);
        Adresa a = new Adresa(addrID, adresa, lokacija);

        return a;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "dajVazeceMeteoPodatkeZaAdresu")
    public MeteoPodaci dajVazeceMeteoPodatkeZaAdresu(@WebParam(name = "adresa") String adresa) {
        Adresa a = dajAdresu(0, adresa);
        ServletContext servletContext = (ServletContext) context.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
        Konfiguracija konfig = (Konfiguracija) servletContext.getAttribute("App_Konfig");
        String APPID = konfig.dajPostavku("OWMAppID");
        OWMKlijent owmk = new OWMKlijent(APPID);
        MeteoPodaci mp = owmk.getRealTimeWeather(a.getGeoloc().getLatitude(), a.getGeoloc().getLongitude());
        return mp;
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "dajZadnjeMeteoPodatkeZaAdresu")
    public MeteoPodaci dajZadnjeMeteoPodatkeZaAdresu(@WebParam(name = "adresa") String adresa) {
        List<MeteoPodaci> listamp = dajSveMeteoPodatkeZaAdresu(adresa);
        if (listamp == null) {
            return null;
        }
        return listamp.get(0);
    }

    /**
     * Web service operation
     */
    @WebMethod(operationName = "dajSveMeteoPodatkeZaAdresu")
    public java.util.List<MeteoPodaci> dajSveMeteoPodatkeZaAdresu(@WebParam(name = "adresa") String adresa) {
        List<MeteoPodaci> listamp = new ArrayList<>();
        try {
            ResultSet rs = DBController.getDbCon().getMeteoInfoForAddress(adresa);
            while (rs.next()) {
                MeteoPodaci mp = new MeteoPodaci();
                mp.setWeatherNumber(rs.getInt(1));
                mp.setWeatherValue(rs.getString(2));
                mp.setTemperatureValue(rs.getFloat(3));
                mp.setTemperatureMin(rs.getFloat(4));
                mp.setTemperatureMax(rs.getFloat(5));
                mp.setHumidityValue(rs.getFloat(6));
                mp.setPressureValue(rs.getFloat(7));
                mp.setWindSpeedValue(rs.getFloat(8));
                mp.setWindDirectionValue(rs.getFloat(9));
                mp.setLastUpdate(rs.getTimestamp(10));
                listamp.add(mp);
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(GeoMeteoWS.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (listamp.isEmpty()) {
            return null;
        }
        return listamp;
    }
}
