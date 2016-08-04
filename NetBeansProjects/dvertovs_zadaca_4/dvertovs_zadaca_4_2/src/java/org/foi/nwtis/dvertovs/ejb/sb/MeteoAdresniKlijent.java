/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.ejb.sb;

import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import org.foi.nwtis.dvertovs.rest.klijenti.GMKlijent;
import org.foi.nwtis.dvertovs.rest.klijenti.OWMKlijent;
import org.foi.nwtis.dvertovs.web.podaci.Lokacija;
import org.foi.nwtis.dvertovs.web.podaci.MeteoPodaci;
import org.foi.nwtis.dvertovs.web.podaci.MeteoPrognoza;

/**
 *
 * @author dare
 */
@Stateless
@LocalBean
public class MeteoAdresniKlijent {

    private String apiKey = "dfsdfsdsjssssiiii"; // TODO PREUZETI IZ KONFIGURACIJE

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    public void postaviKorisnickePodatke(String apiKey) {
        this.apiKey = apiKey;
    }

    public MeteoPodaci dajVazeceMeteoPodatke(String adresa) {
        Lokacija l = this.dajLokaciju(adresa);
        OWMKlijent owmKlijent = new OWMKlijent(apiKey);
        return owmKlijent.getRealTimeWeather(l.getLatitude(), l.getLongitude());
    }

    public Lokacija dajLokaciju(String adresa) {
        GMKlijent gmKlijent = new GMKlijent();
        Lokacija l = gmKlijent.getGeoLocation(adresa);
        return l;
    }

    public List<MeteoPrognoza> dajMeteoPrognoze(String adresa) {
        OWMKlijent owmKlijent = new OWMKlijent(apiKey);
        Lokacija l = this.dajLokaciju(adresa);
        return owmKlijent.getWeatherForecast(l.getLatitude(), l.getLongitude());
    }

}
