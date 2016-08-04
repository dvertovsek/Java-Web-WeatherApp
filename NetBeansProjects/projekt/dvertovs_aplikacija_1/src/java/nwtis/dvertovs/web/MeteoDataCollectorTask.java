/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.web;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import nwtis.dvertovs.db.DBController;
import nwtis.dvertovs.rest.klijenti.OWMKlijent;
import nwtis.dvertovs.web.podaci.Adresa;
import nwtis.dvertovs.web.podaci.Lokacija;
import nwtis.dvertovs.web.podaci.MeteoPodaci;
import nwtis.dvertovs.web.socketserver.StatusMessage;

/**
 *
 * @author dare
 */
public class MeteoDataCollectorTask extends TimerTask implements MeteoDataListener {

    public final String APPID;

    protected volatile boolean isPaused = false;
    protected volatile boolean isStopped = false;

    protected List<Adresa> adressList;

    public MeteoDataCollectorTask(String APPID) {
        this.APPID = APPID;
    }

    @Override
    public void run() {
        if (!isPaused) {
            try {
                ResultSet resultSetAdrese = DBController.getDbCon().getAddresses("","");
                addToAddressList(resultSetAdrese);

                Map<Adresa, MeteoPodaci> adreseMeteoPodaci = getAdreseMeteoPodaciHash();
                System.out.println("Podataka skupljeno: " + adreseMeteoPodaci.size());
                storeToDatabase(adreseMeteoPodaci);

                storeForecastToDB();
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
                Logger.getLogger(MeteoDataCollectorTask.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void addToAddressList(ResultSet rs) throws SQLException {
        this.adressList = new ArrayList<>();
        while (rs.next()) {
            long addrID = rs.getLong(1);
            String addr = rs.getString(2);
            String lat = rs.getString(3);
            String lon = rs.getString(4);
            Adresa adresa = new Adresa(addrID, addr, new Lokacija(lat, lon));
            this.adressList.add(adresa);
        }
    }

    /**
     *
     * @param rs result set svih adresa iz baze
     * @return metoda vraca hash mapu tipa Adresa => MeteoPodaci u kojoj je
     * kljuc adresa iz baze a vrijednost metereoloski podaci za tu adresu
     * @throws SQLException
     */
    private Map<Adresa, MeteoPodaci> getAdreseMeteoPodaciHash() throws SQLException {
        Map<Adresa, MeteoPodaci> map = new HashMap<>();
        this.adressList.stream().forEach((a) -> {
            map.put(a, getMeteoPodaci(a));
        });
        return map;
    }

    /**
     *
     * @param a objekt tipa Adresa
     * @return za dani objekt tipa adresa se vraca objekt tipa MeteoPodaci u
     * kojem se nalaze meteo podaci za danu adresu
     */
    private MeteoPodaci getMeteoPodaci(Adresa a) {
        OWMKlijent owmk = new OWMKlijent(this.APPID);
        MeteoPodaci mp = owmk.getRealTimeWeather(a.getGeoloc().getLatitude(), a.getGeoloc().getLongitude());
        return mp;
    }

    /**
     * metoda sprema u bazu (u tablicu meteo) dane meteo podatke (vrijednost) za
     * danu adresu (kljuc hash mape)
     *
     * @param adreseMeteoPodaci hash mapa tipa Adresa => MeteoPodaci u kojoj je
     * kljuc adresa a vrijednost meteo podaci za tu adresu
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

    private void storeForecastToDB() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        for (Adresa a : this.adressList) {
            for (MeteoPodaci mp : getMeteoPrognozaLista(a)) {
                DBController.getDbCon().insertPrognozaPodatak(a, mp);
            }
        }
    }

    private List<MeteoPodaci> getMeteoPrognozaLista(Adresa a) {
        OWMKlijent owmk = new OWMKlijent(this.APPID);
        List<MeteoPodaci> mp = owmk.getWeatherForecast(a.getGeoloc().getLatitude(), a.getGeoloc().getLongitude());
        return mp;
    }

    @Override
    public boolean startCollecting() {
        if (isPaused) {
            isPaused = false;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean pauseCollecting() {
        if (!isPaused) {
            isPaused = true;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean stopCollecting() {
        if (isStopped) {
            return false;
        } else {
            this.cancel();
            isPaused = true;
            isStopped = true;
            return true;
        }
    }

    @Override
    public String getStatus() {
        if (isStopped) {
            return StatusMessage.STOPPED_02;
        }
        if (isPaused) {
            return StatusMessage.PAUSED_00;
        }
        return StatusMessage.RUNNING_01;
    }

}
