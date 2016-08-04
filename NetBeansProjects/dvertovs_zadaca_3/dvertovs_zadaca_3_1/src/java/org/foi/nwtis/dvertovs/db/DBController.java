/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import org.foi.nwtis.dvertovs.web.podaci.Adresa;
import org.foi.nwtis.dvertovs.web.podaci.MeteoPodaci;

/**
 *
 * @author dare
 */
public final class DBController {

    public static String DatabaseURL;
    public static String DatabaseDriver;
    public static String DatabaseUser;
    public static String DatabasePass;

    private static DBController db;

    private Connection conn;
    private Statement statement;

    /**
     * konstruktor koji kreira vezu prema bazi podataka
     *
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SQLException
     */
    private DBController() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        Class.forName(DatabaseDriver).newInstance();
        this.conn = (Connection) DriverManager.getConnection(DatabaseURL, DatabaseUser, DatabasePass);
    }

    /**
     *
     * @return metoda vraca objekt tipa klase (Singleton)
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     * @throws java.sql.SQLException
     */
    public static synchronized DBController getDbCon() throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        if (db == null) {
            db = new DBController();
        }
        return db;
    }

    /**
     *
     * @return metoda vraca result set svih adresa
     * @throws SQLException
     */
    public ResultSet getAddresses() throws SQLException {
        String selectSQL = "SELECT idadresa, adresa, latitude, longitude FROM adrese";
        PreparedStatement preparedStatement = db.conn.prepareStatement(selectSQL);
        ResultSet rs = preparedStatement.executeQuery();
        return rs;
    }

    /**
     * 
     * @param adresa adresa za koju se vracaju meteoroloski podaci
     * @return metoda vraca result set svih metereoloskih podataka za danu adresu poredanu od najnovije do najstarije
     * @throws SQLException 
     */
    public ResultSet getMeteoInfoForAddress(String adresa) throws SQLException {
        String selectTableSQL = "SELECT vrijeme, vrijemeopis, temp, tempmin, tempmax, vlaga, tlak, vjetar, vjetarsmjer, preuzeto FROM meteo JOIN adrese USING(idadresa) where adresa = ? order by preuzeto desc";
        PreparedStatement preparedStatement = db.conn.prepareStatement(selectTableSQL);
        preparedStatement.setString(1, adresa);
        ResultSet rs = preparedStatement.executeQuery();
        return rs;
    }
    
    /**
     * 
     * @param idadresa idadrese u tablici adrese na temelju koje se vracaju meteo podaci
     * @return metoda vraca result set svih metereoloskih podataka za danu adresu poredanu od najnovije do najstarije
     * @throws SQLException 
     */
    public ResultSet getMeteoInfoForAddress(int idadresa) throws SQLException {
        String selectTableSQL = "SELECT vrijeme, vrijemeopis, temp, tempmin, tempmax, vlaga, tlak, vjetar, vjetarsmjer, preuzeto, adresa FROM meteo JOIN adrese USING(idadresa) where idadresa = ? order by preuzeto desc";
        PreparedStatement preparedStatement = db.conn.prepareStatement(selectTableSQL);
        preparedStatement.setInt(1, idadresa);
        ResultSet rs = preparedStatement.executeQuery();
        return rs;
    }

    /**
     * 
     * @param adresa objekt adrese koji ce se unijeti u bazu
     * @throws SQLException 
     */
    public void insertAddress(Adresa adresa) throws SQLException {
        String insertTableSQL = "INSERT INTO adrese (adresa, latitude, longitude) VALUES (?,?,?)";
        PreparedStatement preparedStatement = db.conn.prepareStatement(insertTableSQL);
        preparedStatement.setString(1, adresa.getAdresa());
        preparedStatement.setString(2, adresa.getGeoloc().getLatitude());
        preparedStatement.setString(3, adresa.getGeoloc().getLongitude());
        preparedStatement.executeUpdate();
    }

    /**
     * 
     * @param adresa adresa za koju ce se unijeti meteo podatak u bazu
     * @param meteoPodatak meteo podataka koji se unosi u bazu
     * @throws SQLException 
     */
    public void insertMeteoPodatak(Adresa adresa, MeteoPodaci meteoPodatak) throws SQLException {
        String insertTableSQL = "INSERT INTO meteo (idadresa, latitude, longitude, adresastanice, vrijeme, vrijemeopis, temp, tempmin, tempmax, vlaga, tlak, vjetar, vjetarsmjer, preuzeto) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = db.conn.prepareStatement(insertTableSQL);
        preparedStatement.setLong(1, adresa.getIdadresa());
        preparedStatement.setString(2, adresa.getGeoloc().getLatitude());
        preparedStatement.setString(3, adresa.getGeoloc().getLongitude());
        preparedStatement.setString(4, "??????????????????????????"); //TODO PITATI NEKOG
        preparedStatement.setLong(5, meteoPodatak.getWeatherNumber());
        preparedStatement.setString(6, meteoPodatak.getWeatherValue());
        preparedStatement.setFloat(7, meteoPodatak.getTemperatureValue());
        preparedStatement.setFloat(8, meteoPodatak.getTemperatureMin());
        preparedStatement.setFloat(9, meteoPodatak.getTemperatureMax());
        preparedStatement.setFloat(10, meteoPodatak.getHumidityValue());
        preparedStatement.setFloat(11, meteoPodatak.getPressureValue());
        preparedStatement.setFloat(12, meteoPodatak.getWindSpeedValue());
        preparedStatement.setFloat(13, meteoPodatak.getWindDirectionValue());
        preparedStatement.setTimestamp(14, new Timestamp(meteoPodatak.getLastUpdate().getTime()));
        preparedStatement.executeUpdate();
    }
}
