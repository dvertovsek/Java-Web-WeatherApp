/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author dare
 */
@Named(value = "slanjePoruke")
@RequestScoped
public class SlanjePoruke {

    private String tkoSalje;
    private String tkoPrima;
    private String predmetPoruke;
    private String tekstPoruke;
    private String tipPoruke = "text/plain";
    String posluzitelj = "localhost"; //todo preuzmi iz konfiguracije
    private String poruka = "";

    /**
     * Creates a new instance of SlanjePoruke
     */
    public SlanjePoruke() {
    }

    public String getTkoSalje() {
        return tkoSalje;
    }

    public void setTkoSalje(String tkoSalje) {
        this.tkoSalje = tkoSalje;
    }

    public String getTkoPrima() {
        return tkoPrima;
    }

    public void setTkoPrima(String tkoPrima) {
        this.tkoPrima = tkoPrima;
    }

    public String getPredmetPoruke() {
        return predmetPoruke;
    }

    public void setPredmetPoruke(String predmetPoruke) {
        this.predmetPoruke = predmetPoruke;
    }

    public String getTekstPoruke() {
        return tekstPoruke;
    }

    public void setTekstPoruke(String tekstPoruke) {
        this.tekstPoruke = tekstPoruke;
    }

    public String getTipPoruke() {
        return tipPoruke;
    }

    public void setTipPoruke(String tipPoruke) {
        this.tipPoruke = tipPoruke;
    }

    /**
     * metoda salje poruku
     * @return metoda vraca tekst poruke rezultata slanje poruke
     */
    public String saljiPoruku() {
        try {
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
            message.setContent(tekstPoruke, this.tipPoruke);

            Transport.send(message);
            poruka = "Poruka je uspjesno poslana.";
            return "OK";

        } catch (Exception e) {
            e.printStackTrace();
            poruka = e.getMessage();
            return "ERROR";
        }
    }

    public String getPoruka() {
        return poruka;
    }
}
