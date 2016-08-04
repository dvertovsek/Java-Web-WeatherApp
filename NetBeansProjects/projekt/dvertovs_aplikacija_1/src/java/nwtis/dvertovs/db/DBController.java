/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Timestamp;
import nwtis.dvertovs.web.podaci.Adresa;
import nwtis.dvertovs.web.podaci.MeteoPodaci;

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

    private final Connection conn;

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

    public ResultSet getUserInfo(String adminName, String adminPass, boolean admin) throws SQLException {
        String selectSQL = "SELECT category,requests FROM dvertovs_users WHERE username = ? AND password = ?";
        if (admin) {
            selectSQL = "SELECT iduser FROM dvertovs_users WHERE username = ? AND password = ? AND idusertype = 1";
        }
        PreparedStatement preparedStatement = db.conn.prepareStatement(selectSQL);
        preparedStatement.setString(1, adminName);
        preparedStatement.setString(2, adminPass);
        ResultSet rs = preparedStatement.executeQuery();
        return rs;
    }

    public void insertUser(String username, String password, String type) throws SQLException, SQLIntegrityConstraintViolationException {
        String insertTableSQL = "INSERT INTO dvertovs_users (username, password, idusertype) VALUES (?, ?, ?)";
        PreparedStatement preparedStatement = db.conn.prepareStatement(insertTableSQL);
        preparedStatement.setString(1, username);
        preparedStatement.setString(2, password);
        if (type.equals("ADMIN")) {
            preparedStatement.setString(3, "1");
        } else {
            preparedStatement.setString(3, "2");
        }
        preparedStatement.executeUpdate();
    }

    public int getUserTypeNum(String type) throws SQLException {
        String selectSQL = "SELECT COUNT(*) FROM dvertovs_users WHERE idusertype = ?";
        PreparedStatement preparedStatement = db.conn.prepareStatement(selectSQL);
        preparedStatement.setString(1, type);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        return rs.getInt(1);
    }

    public int alterUserCategory(String username, String position) throws SQLException, SQLIntegrityConstraintViolationException {
        String selectSQL = "SELECT category FROM dvertovs_users WHERE username = ?";
        PreparedStatement ps = db.conn.prepareStatement(selectSQL);
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        rs.next();
        if(rs.getInt(1) == 1 && position.equals("DOWN") || rs.getInt(1) == 5 && position.equals("UP")){
            throw new SQLIntegrityConstraintViolationException();
        }
        String updateSQL = "UPDATE dvertovs_users SET category = category+1 WHERE username = ?";
        if (position.equals("DOWN")) {
            updateSQL = "UPDATE dvertovs_users SET category = category-1 WHERE username = ?";
        }
        PreparedStatement preparedStatement = db.conn.prepareStatement(updateSQL);
        preparedStatement.setString(1, username);
        return preparedStatement.executeUpdate();
    }

    /**
     *
     * @param adresa
     * @param user
     * @return metoda vraca result set svih adresa
     * @throws SQLException
     */
    public ResultSet getAddresses(String adresa, String user) throws SQLException {
        String selectSQL = "SELECT idadresa, adresa, latitude, longitude FROM dvertovs_adrese";
        if (!adresa.equals("")) {
            selectSQL = "SELECT idadresa, adresa, latitude, longitude FROM dvertovs_adrese WHERE adresa = ?";
        }
        if(!user.equals("")){
            selectSQL = "SELECT idadresa, adresa, latitude, longitude FROM dvertovs_adrese WHERE iduser = (SELECT iduser FROM dvertovs_users WHERE username = ?)";
        }
        PreparedStatement preparedStatement = db.conn.prepareStatement(selectSQL);
        if (!adresa.equals("")) {
            preparedStatement.setString(1, adresa);
        }
        if (!user.equals("")) {
            preparedStatement.setString(1, user);
        }
        ResultSet rs = preparedStatement.executeQuery();
        return rs;
    }

    /**
     *
     * @param adresa adresa za koju ce se unijeti meteo podatak u bazu
     * @param meteoPodatak meteo podataka koji se unosi u bazu
     * @throws SQLException
     */
    public void insertMeteoPodatak(Adresa adresa, MeteoPodaci meteoPodatak) throws SQLException {
        String insertTableSQL = "INSERT INTO dvertovs_meteo (idadresa, vrijemeopis, temp, tempmin, tempmax, vlaga, tlak, vjetar, vjetarsmjer, vrijemeazuriranjastanice, preuzeto) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = db.conn.prepareStatement(insertTableSQL);
        preparedStatement.setLong(1, adresa.getIdadresa());
        preparedStatement.setString(2, meteoPodatak.getWeatherValue());
        preparedStatement.setFloat(3, meteoPodatak.getTemperatureValue());
        preparedStatement.setFloat(4, meteoPodatak.getTemperatureMin());
        preparedStatement.setFloat(5, meteoPodatak.getTemperatureMax());
        preparedStatement.setFloat(6, meteoPodatak.getHumidityValue());
        preparedStatement.setFloat(7, meteoPodatak.getPressureValue());
        preparedStatement.setFloat(8, meteoPodatak.getWindSpeedValue());
        try{
        preparedStatement.setFloat(9, meteoPodatak.getWindDirectionValue());
        }catch(NullPointerException ex){
            preparedStatement.setFloat(9, 0.0f);
        }
        preparedStatement.setTimestamp(10, new Timestamp(meteoPodatak.getLastUpdate().getTime()));
        preparedStatement.setTimestamp(11, new Timestamp(meteoPodatak.getPreuzeto().getTime()));
        preparedStatement.executeUpdate();
    }

    public void insertPrognozaPodatak(Adresa adresa, MeteoPodaci meteoPodatak) throws SQLException {
        String insertTableSQL = "INSERT INTO dvertovs_prognoza (idadresa, vrijemeopis, temp, vlaga, tlak, vrijemeprognoze, preuzeto) VALUES (?,?,?,?,?,?,?)";
        PreparedStatement preparedStatement = db.conn.prepareStatement(insertTableSQL);
        preparedStatement.setLong(1, adresa.getIdadresa());
        preparedStatement.setString(2, meteoPodatak.getWeatherValue());
        preparedStatement.setFloat(3, meteoPodatak.getTemperatureValue());
        preparedStatement.setFloat(4, meteoPodatak.getHumidityValue());
        preparedStatement.setFloat(5, meteoPodatak.getPressureValue());
        preparedStatement.setTimestamp(6, new Timestamp(meteoPodatak.getLastUpdate().getTime()));
        preparedStatement.setTimestamp(7, new Timestamp(meteoPodatak.getPreuzeto().getTime()));
        preparedStatement.executeUpdate();
    }

    /**
     *
     * @param adresa objekt adrese koji ce se unijeti u bazu
     * @param username
     * @throws SQLException
     * @throws java.sql.SQLIntegrityConstraintViolationException
     */
    public void insertAddress(Adresa adresa, String username) throws SQLException, SQLIntegrityConstraintViolationException {
        String insertTableSQL = "INSERT INTO dvertovs_adrese (adresa, latitude, longitude, iduser) VALUES (?,?,?, (SELECT iduser FROM dvertovs_users WHERE username = ?))";
        PreparedStatement preparedStatement = db.conn.prepareStatement(insertTableSQL);
        preparedStatement.setString(1, adresa.getAdresa());
        preparedStatement.setString(2, adresa.getGeoloc().getLatitude());
        preparedStatement.setString(3, adresa.getGeoloc().getLongitude());
        preparedStatement.setString(4, username);
        preparedStatement.executeUpdate();
    }

    public void incrementUserRequest(String user) throws SQLException {
        String updateSQL = "UPDATE dvertovs_users SET requests = requests+1 WHERE username = ?";
        PreparedStatement preparedStatement = db.conn.prepareStatement(updateSQL);
        preparedStatement.setString(1, user);
        preparedStatement.executeUpdate();
    }

    public void resetUserRequests() throws SQLException {
        String updateSQL = "UPDATE dvertovs_users SET requests = 0;";
        db.conn.prepareStatement(updateSQL).executeUpdate();
    }

    public ResultSet getTopAddresses(int limit) throws SQLException{
        String selSQL = "select idadresa, latitude, longitude, adresa, vrijemeopis, count(*) from dvertovs_adrese full join dvertovs_meteo USING(idadresa) GROUP BY adresa ORDER BY count(*) DESC LIMIT ?";
        PreparedStatement ps = db.conn.prepareStatement(selSQL);
        ps.setInt(1, limit);
        ResultSet rs = ps.executeQuery();
        return rs;
    }
    
    /**
     *
     * @param adresa adresa za koju se vracaju meteoroloski podaci
     * @param limit
     * @return metoda vraca result set svih metereoloskih podataka za danu
     * adresu poredanu od najnovije do najstarije
     * @throws SQLException
     */
    public ResultSet getMeteoInfoForAddress(String adresa, int limit) throws SQLException {
        String selectTableSQL = "SELECT vrijemeopis, temp, tempmin, tempmax, vlaga, tlak, vjetar, vjetarsmjer, vrijemeazuriranjastanice, preuzeto FROM dvertovs_meteo JOIN dvertovs_adrese USING(idadresa) where adresa = ? order by preuzeto desc limit ?";
        PreparedStatement preparedStatement = db.conn.prepareStatement(selectTableSQL);
        preparedStatement.setString(1, adresa);
        preparedStatement.setInt(2, limit);
        ResultSet rs = preparedStatement.executeQuery();
        return rs;
    }

    public ResultSet getMeteoInfoForAddress(String adresa, java.util.Date pocetniDatum, java.util.Date krajnjiDatum) throws SQLException {
        String selectTableSQL = "SELECT vrijemeopis, temp, tempmin, tempmax, vlaga, tlak, vjetar, vjetarsmjer, vrijemeazuriranjastanice, preuzeto FROM dvertovs_meteo JOIN dvertovs_adrese USING(idadresa) where adresa = ? AND (vrijemeazuriranjastanice BETWEEN ? AND ?) order by preuzeto desc";
        PreparedStatement preparedStatement = db.conn.prepareStatement(selectTableSQL);
        preparedStatement.setString(1, adresa);
        preparedStatement.setTimestamp(2, new Timestamp(pocetniDatum.getTime()));
        preparedStatement.setTimestamp(3, new Timestamp(krajnjiDatum.getTime()));
        ResultSet rs = preparedStatement.executeQuery();
        return rs;
    }

    public ResultSet getLastForecast(int id) throws SQLException {
        String SQL = "SELECT vrijemeopis, temp, vlaga, tlak, vrijemeprognoze, preuzeto FROM dvertovs_prognoza INNER JOIN (SELECT preuzeto FROM dvertovs_prognoza WHERE idadresa = ? ORDER BY preuzeto DESC LIMIT 1) AS tab1 USING(preuzeto) ORDER BY preuzeto DESC, vrijemeprognoze ASC";
        PreparedStatement ps = db.conn.prepareStatement(SQL);
        ps.setInt(1, id);
        return ps.executeQuery();
    }

    public ResultSet getSpecificForecast(int id, java.util.Date datum) throws SQLException {
        String SQL = "SELECT vrijemeopis, temp, vlaga, tlak, vrijemeprognoze, preuzeto FROM dvertovs_prognoza WHERE idadresa = ? AND vrijemeprognoze = ?";
        PreparedStatement ps = db.conn.prepareStatement(SQL);
        ps.setInt(1, id);
        ps.setTimestamp(2, new Timestamp(datum.getTime()));
        return ps.executeQuery();
    }

    public void writeToLog(String username, String serviceType, String serviceName) throws SQLException {
        String SQL = "INSERT INTO dvertovs_dnevnik_WS(iduser, vrsta, naziv) VALUES ((SELECT iduser from dvertovs_users WHERE username = ?), ?, ?)";
        PreparedStatement ps = db.conn.prepareStatement(SQL);
        ps.setString(1, username);
        ps.setString(2, serviceType);
        ps.setString(3, serviceName);
        ps.executeUpdate();
    }
    
    

}
