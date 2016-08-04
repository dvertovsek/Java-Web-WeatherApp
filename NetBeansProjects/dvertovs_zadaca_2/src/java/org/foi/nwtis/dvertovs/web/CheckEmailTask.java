/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.web;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.ContentType;
import javax.mail.internet.MimeMessage;

import javax.mail.Flags;

import javax.mail.search.FlagTerm;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimerTask;
import org.foi.nwtis.dvertovs.web.zrna.SlanjePoruke;

/**
 *
 * @author dare
 */
public class CheckEmailTask extends TimerTask {

    private final String messagesFolderPath;
    private final String othersFolderPath;
    private final String savedWebsitesPath;

    private String IMAPPort;
    private String address;
    private String user;
    private String pass;
    private String subjectKeyword;

    private String adminEmail;
    private String adminSubject;

    private int redniBrojPoruke;
    private int msgCount;
    private Date processingStart;
    private Date processingEnd;
    CommandExecutor commandExecutor;

    /**
     * 
     * @param path puna putanja do WEB-INF direktorija
     * @param correctMessagesFolder Naziv direktorija gdje ce se spremati ispravne poruke
     * @param otherFolder Naziv direktorija gdje ce se spremati neispravne poruke
     * @param dataFolder Naziv direktorija gdje ce se spremati preuzete web stranice
     */
    public CheckEmailTask(String path, String correctMessagesFolder, String otherFolder, String dataFolder) {
        this.othersFolderPath = path + otherFolder;
        this.messagesFolderPath = path + correctMessagesFolder;
        this.savedWebsitesPath = path + dataFolder;
    }

    @Override
    public void run() {
        try {
            this.processingStart = new Date();
            checkMailbox();
            this.processingEnd = new Date();
            sendMail();
            System.out.println("Dretva: " + Thread.currentThread().getName());
        } catch (MessagingException | IOException | ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(CheckEmailTask.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * 
     * @throws MessagingException
     * @throws IOException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SQLException 
     * 
     * Metoda koja provjerava postanski sanducic. Nakon sto ucita sve poruke tipa MimeMessage koje nemaju postavljenu zastavicu 'Seen' - neprocitane poruke, provjerava jeli poruka tipa
     * text/html ili text/plain te ovisno o ishodu provjere gradi tekst poruke u varijablu tipa String. Izvrsava se naredba poslana unutar email poruke pozivanjem klase CommandExecutor,
     * te se naposlijetku email poruka sprema na disk.
     */
    private void checkMailbox() throws MessagingException, IOException, ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {
        java.util.Properties properties = System.getProperties();
        properties.put("mail.imap.host", address);
        properties.put("mail.imap.port", IMAPPort);

        Session session = Session.getInstance(properties, null);

        Store store = session.getStore("imap");
        store.connect(address, user, pass);

        Folder folder = store.getFolder("INBOX");
        folder.open(Folder.READ_ONLY);

        Message messages[] = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
        this.msgCount = messages.length;

        commandExecutor = new CommandExecutor();
        System.out.println("messages.length---" + messages.length);

        for (int i = 0; i < messages.length; ++i) {
            MimeMessage message = (MimeMessage) messages[i];
            message.setFlag(Flags.Flag.SEEN, true);

            String msgSubject = message.getSubject();
            boolean correctMessage = false;
            if (this.subjectKeyword.equals(msgSubject)) {

                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                //System.out.println("Text: " + message.getContent().toString());

                ContentType ct = new ContentType(message.getContentType());
                String s = "";
                if ("text/html".equalsIgnoreCase(ct.getBaseType())) {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(message.getInputStream()));
                    String temp;
                    while ((temp = reader.readLine()) != null) {
                        System.out.println("dio Stringa: " + s);
                        s += temp;
                    }
                } else {
                    Object obj = message.getContent();
                    if (obj instanceof String) {
                        System.out.println("stinrg else: " + (String) obj);
                        s = (String) obj;
                    }
                }
                if (commandExecutor.executeCommand(s, this.savedWebsitesPath)) {
                    correctMessage = true;
                }
            }
            if (!correctMessage) {
                saveEmailToFile(message, this.othersFolderPath);
            } else {
                saveEmailToFile(message, this.messagesFolderPath);
            }
        }
    }

    /**
     * 
     * @param message varijabla tipa MimeMessage ciji ce sadrzaj biti spremljen na disk
     * @param folder putanja do direktorija na koji ce poruka biti spremljena
     * @throws IOException
     * @throws MessagingException 
     */
    private void saveEmailToFile(MimeMessage message, String folder) throws IOException, MessagingException {
        File dir = new File(folder);
        if (!dir.exists()) {
            dir.mkdir();
        }
        Date today = new Date();
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String date = DATE_FORMAT.format(today);
        String path = folder + File.separator + "mail_" + date + "_" + message.getSubject() + ".eml";
        message.writeTo(new FileOutputStream(new File(path)));
    }

    /**
     * metoda za slanje administratorske email poruke statistike, koja se poziva na kraju svakog intervala dretve
     */
    private void sendMail() {
        SlanjePoruke slanjePoruke = new SlanjePoruke();

        DecimalFormat df = new DecimalFormat("#,##0");
        String subject = this.adminSubject + " " + df.format(this.redniBrojPoruke);
        slanjePoruke.setPredmetPoruke(subject);
        this.redniBrojPoruke++;
        slanjePoruke.setTkoPrima(this.adminEmail);
        slanjePoruke.setTkoSalje("dretvaEvidencija");
        slanjePoruke.setTipPoruke("text/html");
        slanjePoruke.setTekstPoruke(generateReportMessage());
        slanjePoruke.saljiPoruku();
    }

    /**
     * 
     * @return metoda koja vraca izvjestaj koji treba biti poslan u sklopu administratorske email poruke
     */
    private String generateReportMessage() {
        String message = "";

        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy hh.mm.ss.zzz");
        message += "<ul>";
        message += "<li>Obrada zapocela u " + DATE_FORMAT.format(this.processingStart) + "</li>";
        message += "<li>Obrada zavrsila u " + DATE_FORMAT.format(this.processingEnd) + "</li>";
        message += "</ul>";
        message += "<ul>";

        long trajanjeObrade = this.processingEnd.getTime() - this.processingStart.getTime();
        message += "<ul>";
        message += "<li>Trajanje obrade u ms: " + trajanjeObrade + "</li>";
        message += "<li>Broj poruka: " + this.msgCount + "</li>";
        message += "<li>Broj dodanih podataka GRAD: " + this.commandExecutor.getAddGRADCount() + "</li>";
        message += "<li>Broj azuriranih podataka GRAD: " + this.commandExecutor.getUpdateGRADCount() + "</li>";
        message += "<li>Broj dodanih podataka TVRTKA: " + this.commandExecutor.getAddTVRTKACount() + "</li>";
        message += "<li>Broj azuriranih podataka TVRTKA: " + this.commandExecutor.getUpdateTVRTKACount() + "</li>";
        message += "</ul>";
        return message;
    }

    public void setIMAPPort(String IMAPPort) {
        this.IMAPPort = IMAPPort;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setSubjectKeyword(String subjectKeyword) {
        this.subjectKeyword = subjectKeyword;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public void setAdminSubject(String adminSubject) {
        this.adminSubject = adminSubject;
    }
}
