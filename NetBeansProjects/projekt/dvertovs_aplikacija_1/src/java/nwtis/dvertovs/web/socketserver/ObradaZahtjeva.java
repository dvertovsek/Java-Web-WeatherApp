/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.web.socketserver;

import nwtis.dvertovs.web.MeteoDataListener;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import nwtis.dvertovs.db.DBController;
import nwtis.dvertovs.rest.klijenti.GMKlijent;
import nwtis.dvertovs.web.podaci.Adresa;
import nwtis.dvertovs.web.podaci.Lokacija;
import org.foi.nwtis.dvertovs.konfiguracije.*;

/**
 *
 * @author grupa_1
 */
public class ObradaZahtjeva extends Thread {

    private final String adminRegex = "USER ([A-Za-z0-9]{1,}); PASSWD ([A-Za-z0-9]{1,});( PAUSE| START| STOP| STATUS| ADD ([A-Za-z0-9]{1,}); PASSWD ([A-Za-z0-9]{1,}); ROLE (ADMIN|USER)| (UP|DOWN) ([A-Za-z0-9]{1,}))?;?";
    private final String userRegex = "USER ([A-Za-z0-9]{1,}); PASSWD ([A-Za-z0-9]{1,});( TEST| GET| ADD) ([A-Za-z0-9 ,-]{1,});";

    public MeteoDataListener meteoDataListener;

    protected Konfiguracija konfig = null;
    protected java.util.Date vrijemePrimanjaZahtjeva;
    protected String komanda;

    protected boolean suspendFlag;

    protected Socket socket;

    /**
     *
     * @param tg grupa dretvi (klasa ThreadGroup) u koju će se staviti
     * novokreirana dretva
     * @param threadName ime novokreirane dretve
     * @param konfig Konfiguracijska datoteka
     * @param vrijemePrimanjaZahtjeva
     */
    public ObradaZahtjeva(ThreadGroup tg, String threadName, Konfiguracija konfig, java.util.Date vrijemePrimanjaZahtjeva) {
        super(tg, threadName);
        this.konfig = konfig;
        this.vrijemePrimanjaZahtjeva = vrijemePrimanjaZahtjeva;
        suspendFlag = false;
    }

    @Override
    public void interrupt() {
        super.interrupt(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * nadjačana metoda iz klase thread, unutar metode se unutar beskonačne
     * petlje prihvaća zahtjev, te suspendira dretva nakon obrađenog zahtjeva.
     * Dretva čeka ukoliko je suspendirana.
     */
    @Override
    public void run() {
        System.out.println(getName());
        while (true) {

            try {
                this.komanda = acceptRequest();
                String response = processRequest(this.komanda);
                sendResponse(response);
            } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException | MessagingException ex) {
                Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
            } finally {
                suspendThread();
            }

            synchronized (this) {
                while (suspendFlag) {
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(ObradaZahtjeva.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
    }

    /**
     * metoda koja postavlja zastavicu te suspendira dretvu na određeno vrijeme
     * (dok čeka zahtjev)
     */
    private void suspendThread() {
        suspendFlag = true;
    }

    /**
     * metoda koja postavlja zastavicu te budi dretvu kako bi mogla prihvatiti
     * zahtjev
     */
    public synchronized void resumeThread() {
        suspendFlag = false;
        notify();
    }

    /**
     *
     * @param socket priključnica koja se prosljeđuje dretvi prije nego joj se
     * dodjeli obrada zahtjeva. Unutar priključnice se nalaze podaci o zahtjevu
     * klijenta.
     */
    void setSocket(Socket socket) {
        this.socket = socket;
    }

    /**
     * metoda prihvaća zahtjev iz priključnice te ga šalje na obradu
     *
     * @throws IOException ukoliko postoji problem pri čitanju toka podataka iz
     * priključnice, generira se iznimka
     */
    private String acceptRequest() throws IOException {
        InputStream is = socket.getInputStream();
        StringBuilder req = new StringBuilder();
        int bajt;
        while ((bajt = is.read()) != -1) {
            req.append((char) bajt);
        }
        return req.toString();
    }

    /**
     *
     * @param response odgovor koji se šalje serveru
     * @throws IOException ukoliko postoji problem pri slanju na vanjski tok
     * podataka generira se iznimka
     */
    private void sendResponse(String response) throws IOException {
        OutputStream os = socket.getOutputStream();
        os.write(response.getBytes());
        os.flush();
        os.close();
    }

    /**
     *
     * @param req tekst zahtjeva koji je došao na obradu.
     * @return vraća se ili valjano tijelo odgovora na zahtjev ili poruka o
     * grešci koja se šalje natrag klijentu
     */
    private String processRequest(String req) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, MessagingException {
        System.out.println("Request: " + req);

        String responseBody = "";

        Matcher adminMatcher = Pattern.compile(adminRegex).matcher(req);
        Matcher userMatcher = Pattern.compile(userRegex).matcher(req);

        boolean matched = false;
        if (adminMatcher.matches()) {
            if (DBController.getDbCon().getUserInfo(adminMatcher.group(1), adminMatcher.group(2), false).next()) {
                matched = true;
            }
        }
        if (userMatcher.matches()) {
            if (DBController.getDbCon().getUserInfo(userMatcher.group(1), userMatcher.group(2), false).next()) {
                matched = true;
            }
        }
        if (!matched) {
            return StatusMessage.AUTHENTICATION_FAILED_20;
        }
        
        if (Pattern.compile("USER ([A-Za-z0-9]{1,}); PASSWD ([A-Za-z0-9]{1,});(?![ A-Za-z0-9]{1,})").matcher(req).matches()) {
            return StatusMessage.OK_10;
        } else if (adminMatcher.matches()) {
            responseBody = processAdminRequest(adminMatcher);
        } else if (userMatcher.matches()) {
            responseBody = processUserRequest(userMatcher);
        }
        return responseBody;
    }

    /**
     *
     * @param m Matcher u kojem se nalaze sve matchirane grupe iz regularne
     * ekspresije administratorskog zahtjeva
     * @return metoda vraća poruku odgovora na temelju zahtjeva
     */
    private String processAdminRequest(Matcher m) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, MessagingException {

        ResultSet userRS = DBController.getDbCon().getUserInfo(m.group(1), m.group(2), true);

        if (userRS.next()) {
            switch (m.group(3)) {
                case " PAUSE":
                    if (!meteoDataListener.pauseCollecting()) {
                        return StatusMessage.ALREADY_PAUSED_30;
                    }
                    break;
                case " START":
                    if (!meteoDataListener.startCollecting()) {
                        return StatusMessage.ALREADY_RUNNING_31;
                    }
                    break;
                case " STATUS":
                    return meteoDataListener.getStatus();
                case " STOP":
                    if (!meteoDataListener.stopCollecting()) {
                        return StatusMessage.ALREADY_STOPPED_32;
                    }
                    break;
                default:
                    return manageUsers(m.group(3));
            }
        } else {
            return StatusMessage.AUTHORIZATION_FAILED_21;
        }
        return StatusMessage.OK_10;
    }

    private String manageUsers(String userCommand) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, MessagingException {
        String addUserRegex = " ADD ([A-Za-z0-9]{1,}); PASSWD ([A-Za-z0-9]{1,}); ROLE (ADMIN|USER)";
        String alterUserCategoryRegex = " (UP|DOWN) ([A-Za-z0-9]{1,})";

        Matcher addUserMatcher = Pattern.compile(addUserRegex).matcher(userCommand);
        Matcher alterUserCategoryMatcher = Pattern.compile(alterUserCategoryRegex).matcher(userCommand);
        if (addUserMatcher.matches()) {
            boolean constraintViolated = false;
            try {
                DBController.getDbCon().insertUser(addUserMatcher.group(1), addUserMatcher.group(2), addUserMatcher.group(3));
            } catch (SQLIntegrityConstraintViolationException ex) {
                constraintViolated = true;
            }
            int adminNum = DBController.getDbCon().getUserTypeNum("1");
            int normalNum = DBController.getDbCon().getUserTypeNum("2");
            saljiPoruku(adminNum, normalNum);

            if (constraintViolated) {
                return StatusMessage.USER_ALREADY_EXISTS_33;
            }

        } else if (alterUserCategoryMatcher.matches()) {
            try {
                if (DBController.getDbCon().alterUserCategory(alterUserCategoryMatcher.group(2), alterUserCategoryMatcher.group(1)) == 0) {
                    return StatusMessage.USER_DOESNT_EXIST_35;
                }
            } catch (SQLIntegrityConstraintViolationException ex) {
                return StatusMessage.CANNOT_CHANGE_CATEGORY_34;
            }
        }
        return StatusMessage.OK_10;
    }

    public void saljiPoruku(int brAdmin, int brObicni) throws MessagingException {

        String tkoSalje = konfig.dajPostavku("posiljateljPoruke");
        String tkoPrima = konfig.dajPostavku("primateljPoruke");
        String predmetPoruke = konfig.dajPostavku("predmetPoruke");
        String tipPoruke = "text/plain";
        String posluzitelj = "localhost";

        String tekstPoruke = "";
        tekstPoruke += "Sadrzaj komande : " + this.komanda + "\n";
        tekstPoruke += "Vrijeme primanja zahtjeva : " + this.vrijemePrimanjaZahtjeva + "\n";
        tekstPoruke += "Uk. broj korisnika : " + (brAdmin + brObicni) + "\n";
        tekstPoruke += "Br. admin korisnika : " + brAdmin + "\n";
        tekstPoruke += "Br. obicnih korisnika : " + brObicni + "\n";

        // Create the JavaMail session
        java.util.Properties properties = System.getProperties();
        properties.put("mail.smtp.host", posluzitelj);

        Session session = Session.getInstance(properties, null);

        // Construct the message
        MimeMessage message = new MimeMessage(session);

        // Set the from address
        Address fromAddress = new InternetAddress(tkoSalje);
        message.setFrom(fromAddress);

        // Parse and set the recipient addresses
        Address[] toAddresses = InternetAddress.parse(tkoPrima);
        message.setRecipients(Message.RecipientType.TO, toAddresses);

        // Set the subject and text
        message.setSubject(predmetPoruke);
        //message.setText(tekstPoruke);
        message.setContent(tekstPoruke, tipPoruke);

        Transport.send(message);
        System.out.println("Message sent");
    }

    /**
     *
     * @param m Matcher u kojem se nalaze sve matchirane grupe iz regularne
     * ekspresije korisničkog zahtjeva
     * @return metoda vraća poruku odgovora na temelju zahtjeva
     */
    private String processUserRequest(Matcher m) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        String username = m.group(1);
        String pass = m.group(2);
        DBController.getDbCon().incrementUserRequest(username);
        ResultSet resultset = DBController.getDbCon().getUserInfo(username, pass, false);
        int kvota = Integer.parseInt(this.konfig.dajPostavku("kvotaZahtjeva"));
        resultset.next();
        if (kvota * resultset.getInt("category") < resultset.getInt("requests")) {
            return StatusMessage.NO_REQUESTS_LEFT_40;
        }

        String cmd = m.group(3);
        String adresaNaziv = m.group(4);
        DBController.getDbCon().writeToLog(username, "SS", adresaNaziv);
        switch (cmd) {
            case " TEST":
                ResultSet rs = DBController.getDbCon().getAddresses(adresaNaziv,"");
                if (!rs.next()) {
                    return StatusMessage.ADRESS_DOESNT_EXIST_42;
                }
                break;
            case " ADD":
                Lokacija lokacija = new GMKlijent().getGeoLocation(adresaNaziv);
                Adresa adresa = new Adresa(0, adresaNaziv, lokacija);
                try {
                    DBController.getDbCon().insertAddress(adresa, username);
                } catch (SQLIntegrityConstraintViolationException ex) {
                    return StatusMessage.ADRESS_ALREADY_EXISTS_41;
                }
                break;
            //case " GET":
            //  break;            //case " GET":
            //  break;
        }
        return StatusMessage.OK_10;
    }

}
