/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.ws.serveri;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.annotation.Resource;
import javax.jws.WebService;
import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.servlet.ServletContext;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import nwtis.dvertovs.db.DBController;
import nwtis.dvertovs.web.podaci.Adresa;
import nwtis.dvertovs.web.podaci.Lokacija;
import nwtis.dvertovs.web.podaci.MeteoPodaci;
import org.foi.nwtis.dvertovs.konfiguracije.Konfiguracija;

/**
 *
 * @author dare
 */
@WebService(serviceName = "DvertovsAplikacija1WS")
public class DvertovsAplikacija1WS {

    @Resource
    private WebServiceContext context;

    private final String userRegex = "USER ([A-Za-z0-9]{1,}); PASSWD ([A-Za-z0-9]{1,});(?![ A-Za-z0-9]{1,})";

    /**
     * Web service operation
     *
     * @param auth
     * @param adresa
     * @return
     */
    @WebMethod(operationName = "dajZadnjeMeteoPodatke")
    public MeteoPodaci dajZadnjeMeteoPodatke(@WebParam(name = "auth") String auth, @WebParam(name = "adresa") String adresa) {
        if (userAuthenticated(auth, this.getQuota(), "SOAP", "dajZadnjeMeteoPodatke")) {

            ResultSet rs;
            try {
                rs = DBController.getDbCon().getMeteoInfoForAddress(adresa, 1);
                if (rs.next()) {
                    MeteoPodaci mp = new MeteoPodaci();
                    mp.setWeatherValue(rs.getString("vrijemeopis"));
                    mp.setTemperatureValue(rs.getFloat("temp"));
                    mp.setTemperatureMin(rs.getFloat("tempmin"));
                    mp.setTemperatureMax(rs.getFloat("tempmax"));
                    mp.setPressureValue(rs.getFloat("tlak"));
                    mp.setHumidityValue(rs.getFloat("vlaga"));
                    mp.setWindSpeedValue(rs.getFloat("vjetar"));
                    mp.setWindDirectionValue(rs.getFloat("vjetarsmjer"));
                    mp.setLastUpdate(rs.getTimestamp("vrijemeazuriranjastanice"));
                    mp.setPreuzeto(rs.getTimestamp("preuzeto"));
                    return mp;
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
                Logger.getLogger(DvertovsAplikacija1WS.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return null;
    }

    /**
     * Web service operation
     *
     * @param auth
     * @return
     */
    @WebMethod(operationName = "dajSveAdrese")
    public List<nwtis.dvertovs.web.podaci.Adresa> dajSveAdrese(@WebParam(name = "auth") String auth) {
        List<Adresa> listaAdresa = new ArrayList<>();
        if (userAuthenticated(auth, this.getQuota(), "SOAP", "dajSveAdrese")) {
            ResultSet rs;
            try {
                Matcher m = Pattern.compile(userRegex).matcher(auth);
                m.matches();
                rs = DBController.getDbCon().getAddresses("",m.group(1));
                while (rs.next()) {
                    Adresa a = new Adresa();
                    a.setIdadresa(rs.getInt("idadresa"));
                    a.setAdresa(rs.getString("adresa"));
                    Lokacija l = new Lokacija(rs.getString("latitude"), rs.getString("longitude"));
                    a.setGeoloc(l);
                    listaAdresa.add(a);
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
                Logger.getLogger(DvertovsAplikacija1WS.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return listaAdresa;
    }

    /**
     * Web service operation
     *
     * @param auth
     * @param limit
     * @return
     */
    @WebMethod(operationName = "dajTopAdrese")
    public List<Adresa> dajTopAdrese(@WebParam(name = "auth") String auth, @WebParam(name = "limit") int limit) {
        List<Adresa> listaAdresa = new ArrayList<>();
        if (userAuthenticated(auth, this.getQuota(), "SOAP", "dajTopAdrese")) {

            ResultSet rs;
            try {
                rs = DBController.getDbCon().getTopAddresses(limit);
                while (rs.next()) {
                    Adresa a = new Adresa();
                    a.setIdadresa(rs.getInt("idadresa"));
                    a.setAdresa(rs.getString("adresa"));
                    Lokacija l = new Lokacija(rs.getString("latitude"), rs.getString("longitude"));
                    a.setGeoloc(l);
                    listaAdresa.add(a);
                }
            } catch (SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {
                Logger.getLogger(DvertovsAplikacija1WS.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return listaAdresa;
    }

    /**
     * Web service operation
     *
     * @param auth
     * @param adresa
     * @param limit
     * @return
     */
    @WebMethod(operationName = "dajZadnjihNPodatakaZaAdresu")
    public List<MeteoPodaci> dajZadnjihNPodatakaZaAdresu(@WebParam(name = "auth") String auth, @WebParam(name = "adresa") String adresa, @WebParam(name = "limit") int limit) {
        List<MeteoPodaci> listamp = new ArrayList<>();
        if (userAuthenticated(auth, this.getQuota(), "SOAP", "dajZadnjihNPodatakaZaAdresu")) {

            ResultSet rs;
            try {
                rs = DBController.getDbCon().getMeteoInfoForAddress(adresa, limit);
                while (rs.next()) {
                    MeteoPodaci mp = new MeteoPodaci();
                    mp.setWeatherValue(rs.getString("vrijemeopis"));
                    mp.setTemperatureValue(rs.getFloat("temp"));
                    mp.setTemperatureMin(rs.getFloat("tempmin"));
                    mp.setTemperatureMax(rs.getFloat("tempmax"));
                    mp.setPressureValue(rs.getFloat("tlak"));
                    mp.setHumidityValue(rs.getFloat("vlaga"));
                    mp.setWindSpeedValue(rs.getFloat("vjetar"));
                    mp.setWindDirectionValue(rs.getFloat("vjetarsmjer"));
                    mp.setLastUpdate(rs.getTimestamp("vrijemeazuriranjastanice"));
                    mp.setPreuzeto(rs.getTimestamp("preuzeto"));
                    listamp.add(mp);
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
                Logger.getLogger(DvertovsAplikacija1WS.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return listamp;
    }

    /**
     * Web service operation
     *
     * @param auth
     * @param adresa
     * @param pocetniDatum
     * @param krajnjiDatum
     * @return
     * @throws java.text.ParseException
     * @throws java.sql.SQLException
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.InstantiationException
     */
    @WebMethod(operationName = "dajMeteoPodatkeUIntervalu")
    public List<MeteoPodaci> dajMeteoPodatkeUIntervalu(@WebParam(name = "auth") String auth, @WebParam(name = "adresa") String adresa, @WebParam(name = "pocetniDatum") String pocetniDatum, @WebParam(name = "krajnjiDatum") String krajnjiDatum) throws ParseException {
        List<MeteoPodaci> listamp = new ArrayList<>();
        if (userAuthenticated(auth, this.getQuota(), "SOAP", "dajMeteoPodatkeUIntervalu")) {

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            ResultSet rs;

            try {
                rs = DBController.getDbCon().getMeteoInfoForAddress(adresa, df.parse(pocetniDatum), df.parse(krajnjiDatum));
                while (rs.next()) {
                    MeteoPodaci mp = new MeteoPodaci();
                    mp.setWeatherValue(rs.getString("vrijemeopis"));
                    mp.setTemperatureValue(rs.getFloat("temp"));
                    mp.setTemperatureMin(rs.getFloat("tempmin"));
                    mp.setTemperatureMax(rs.getFloat("tempmax"));
                    mp.setPressureValue(rs.getFloat("tlak"));
                    mp.setHumidityValue(rs.getFloat("vlaga"));
                    mp.setWindSpeedValue(rs.getFloat("vjetar"));
                    mp.setWindDirectionValue(rs.getFloat("vjetarsmjer"));
                    mp.setLastUpdate(rs.getTimestamp("vrijemeazuriranjastanice"));
                    mp.setPreuzeto(rs.getTimestamp("preuzeto"));
                    listamp.add(mp);
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
                Logger.getLogger(DvertovsAplikacija1WS.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        return listamp;
    }

    public boolean userAuthenticated(String auth, int kvota, String serviceType, String serviceName) {

        Matcher m = Pattern.compile(userRegex).matcher(auth);
        if (m.matches()) {

            String username = m.group(1);
            String pass = m.group(2);
            try {
                if (!DBController.getDbCon().getUserInfo(username, pass, false).next()) {
                    return false;
                }

                DBController.getDbCon().writeToLog(username, serviceType, serviceName);

                if (!DBController.getDbCon().getUserInfo(username, pass, true).next()) {

                    DBController.getDbCon().incrementUserRequest(username);

                    ResultSet resultset = DBController.getDbCon().getUserInfo(username, pass, false);
                    resultset.next();
                    if (kvota * resultset.getInt("category") < (resultset.getInt("requests") + 1)) {
                        return false;
                    }
                }
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
                Logger.getLogger(DvertovsAplikacija1WS.class.getName()).log(Level.SEVERE, null, ex);
            }
        }else{
            return false;
        }
        return true;
    }

    protected int getQuota() {
        ServletContext sc = (ServletContext) context.getMessageContext().get(MessageContext.SERVLET_CONTEXT);
        Konfiguracija konfig = (Konfiguracija) sc.getAttribute("App_Konfig");
        return Integer.parseInt(konfig.dajPostavku("kvotaZahtjeva"));
    }
}
