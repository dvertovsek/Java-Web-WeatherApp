/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.ws.klijenti;
/**
 *
 * @author dare
 */
public class MeteoWSKlijent {

    public static java.util.List<org.foi.nwtis.dvertovs.ws.serveri.Adresa> dajSveAdrese() {
        org.foi.nwtis.dvertovs.ws.serveri.GeoMeteoWS_Service service = new org.foi.nwtis.dvertovs.ws.serveri.GeoMeteoWS_Service();
        org.foi.nwtis.dvertovs.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajSveAdrese();
    }

    public static java.util.List<org.foi.nwtis.dvertovs.ws.serveri.MeteoPodaci> dajSveMeteoPodatkeZaAdresu(java.lang.String adresa) {
        org.foi.nwtis.dvertovs.ws.serveri.GeoMeteoWS_Service service = new org.foi.nwtis.dvertovs.ws.serveri.GeoMeteoWS_Service();
        org.foi.nwtis.dvertovs.ws.serveri.GeoMeteoWS port = service.getGeoMeteoWSPort();
        return port.dajSveMeteoPodatkeZaAdresu(adresa);
    }
    
}
