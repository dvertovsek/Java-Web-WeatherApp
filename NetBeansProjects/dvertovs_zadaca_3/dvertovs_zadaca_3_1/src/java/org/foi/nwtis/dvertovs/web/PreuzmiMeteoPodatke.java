/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.web;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.dvertovs.db.DBController;
import org.foi.nwtis.dvertovs.rest.klijenti.OWMKlijent;
import org.foi.nwtis.dvertovs.web.podaci.Adresa;
import org.foi.nwtis.dvertovs.web.podaci.Lokacija;
import org.foi.nwtis.dvertovs.web.podaci.MeteoPodaci;

/**
 *
 * @author dare
 */
public class PreuzmiMeteoPodatke extends TimerTask {

    private final String APPID;

    public PreuzmiMeteoPodatke(String APPID) {
        this.APPID = APPID;
    }

    @Override
    public void run() {
        System.out.println("Dretva: " + Thread.currentThread().getName());
        try {
            ResultSet resultSetAdrese = DBController.getDbCon().getAddresses();
            Map<Adresa, MeteoPodaci> adreseMeteoPodaci = getAdreseMeteoPodaciHash(resultSetAdrese);
            System.out.println("Podataka skupljeno: " + adreseMeteoPodaci.size());
            storeToDatabase(adreseMeteoPodaci);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(PreuzmiMeteoPodatke.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @param rs result set svih adresa iz baze
     * @return metoda kreira i vraca listu tipa Adresa na temelju rezultata iz baze
     * @throws SQLException 
     */
    private ArrayList<Adresa> getAdreseLista(ResultSet rs) throws SQLException {
        ArrayList<Adresa> al = new ArrayList<>();
        while (rs.next()) {
            long addrID = rs.getLong(1);
            String addr = rs.getString(2);
            String lat = rs.getString(3);
            String lon = rs.getString(4);
            Adresa adresa = new Adresa(addrID, addr, new Lokacija(lat, lon));
            al.add(adresa);
        }
        return al;
    }

    /**
     * 
     * @param rs result set svih adresa iz baze
     * @return metoda vraca hash mapu tipa Adresa => MeteoPodaci u kojoj je kljuc adresa iz baze a vrijednost metereoloski podaci za tu adresu
     * @throws SQLException 
     */
    private Map<Adresa, MeteoPodaci> getAdreseMeteoPodaciHash(ResultSet rs) throws SQLException {
        Map<Adresa, MeteoPodaci> map = new HashMap<>();
        while (rs.next()) {
            long addrID = rs.getLong(1);
            String addr = rs.getString(2);
            String lat = rs.getString(3);
            String lon = rs.getString(4);
            Adresa adresa = new Adresa(addrID, addr, new Lokacija(lat, lon));
            map.put(adresa, getMeteoPodaciLista(adresa));
        }
        return map;
    }

    /**
     * 
     * @param a objekt tipa Adresa
     * @return za dani objekt tipa adresa se vraca objekt tipa MeteoPodaci u kojem se nalaze meteo podaci za danu adresu 
     */
    private MeteoPodaci getMeteoPodaciLista(Adresa a) {
        OWMKlijent owmk = new OWMKlijent(this.APPID);
        MeteoPodaci mp = owmk.getRealTimeWeather(a.getGeoloc().getLatitude(), a.getGeoloc().getLongitude());
        return mp;
    }

    /**
     * metoda sprema u bazu (u tablicu meteo) dane meteo podatke (vrijednost) za danu adresu (kljuc hash mape)
     * @param adreseMeteoPodaci hash mapa tipa Adresa => MeteoPodaci u kojoj je kljuc adresa a vrijednost meteo podaci za tu adresu
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException 
     */
    private void storeToDatabase(Map<Adresa, MeteoPodaci> adreseMeteoPodaci) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        for (Map.Entry pair : adreseMeteoPodaci.entrySet()) {
            DBController.getDbCon().insertMeteoPodatak((Adresa) pair.getKey(), (MeteoPodaci) pair.getValue());
        }
    }
}
