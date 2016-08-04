/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.web.kontrole;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

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
     * @param command naredba dobivena iz email poruke (ADD ili UPDATE)
     * @param commandParam parametar naredbe (npr. foi ili varazdin)
     * @return metoda vraca true ukoliko se naredba uspjesno izvrsila
     * @throws SQLException 
     */
    public boolean executeCommand(String command, String commandParam) throws SQLException {
        String selectSQL = "SELECT * FROM elementi WHERE naziv = ?";
        PreparedStatement preparedStatement = db.conn.prepareStatement(selectSQL);
        preparedStatement.setString(1, commandParam);
        ResultSet rs = preparedStatement.executeQuery();
        switch (command) {
            case "ADD":
                if (rs.next()) {
                    return false;
                }
                String insertSQL = "INSERT INTO elementi VALUES(?)";
                preparedStatement = db.conn.prepareStatement(insertSQL);
                preparedStatement.setString(1, commandParam);
                preparedStatement.executeUpdate();
                break;
            case "UPDATE":
                if (!rs.next()) {
                    return false;
                }
                break;
        }
        return true;
    }
}
