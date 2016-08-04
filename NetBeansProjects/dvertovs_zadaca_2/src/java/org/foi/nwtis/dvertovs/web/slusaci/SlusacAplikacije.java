/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.web.slusaci;

import java.io.File;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.foi.nwtis.dvertovs.konfiguracije.Konfiguracija;
import org.foi.nwtis.dvertovs.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.dvertovs.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.dvertovs.konfiguracije.bp.BP_Konfiguracija;
import org.foi.nwtis.dvertovs.web.CheckEmailTask;
import org.foi.nwtis.dvertovs.web.kontrole.DBController;

/**
 * Web application lifecycle listener.
 *
 * @author dare
 */
public class SlusacAplikacije implements ServletContextListener {

    Timer timer;
    
    /**
     * 
     * @param sce
     * metoda cita parametre konfiguracije, prosljedjuje Singletonu DBController podatke potrebne za povezivanje na bazu te pokrece dretvu za citanje emaila
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {

        ServletContext context = sce.getServletContext();
        String konfiguracijaDB = context.getInitParameter("konfiguracija");
        String konfiguracijaApp = context.getInitParameter("konfiguracijaApp");
        String putanje = context.getRealPath("/WEB-INF") + File.separator;

        BP_Konfiguracija konfigDB = null;
        Konfiguracija konfigApp = null;
        try {
            konfigDB = new BP_Konfiguracija(putanje + konfiguracijaDB);
            konfigApp = KonfiguracijaApstraktna.preuzmiKonfiguraciju(putanje + konfiguracijaApp);
            context.setAttribute("BP_Konfig", konfigDB);
            System.out.println("Ucitana konfiguracija.");

            DBController.DatabaseURL = konfigDB.getServerDatabase() + konfigDB.getUserDatabase();
            DBController.DatabaseDriver = konfigDB.getDriverDatabase();
            DBController.DatabaseUser = konfigDB.getUserUsername();
            DBController.DatabasePass = konfigDB.getUserPassword();

        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(SlusacAplikacije.class.getName()).log(Level.SEVERE, null, ex);
        }

        timer = new Timer();
        String othersPath = konfigApp.dajPostavku("direktorijZaSpremanjeOstalih");
        String correctPath = konfigApp.dajPostavku("direktorijZaSpremanjeIspravnih");
        String dataFolder = konfigApp.dajPostavku("direktorijZaSpremanjeWebsite");

        CheckEmailTask checkEmailTask = new CheckEmailTask(putanje, correctPath, othersPath, dataFolder);
        checkEmailTask.setAddress(konfigApp.dajPostavku("adresaPosluzitelj"));
        checkEmailTask.setIMAPPort(konfigApp.dajPostavku("IMAPPort"));
        checkEmailTask.setUser(konfigApp.dajPostavku("adresaInbox"));
        checkEmailTask.setPass(konfigApp.dajPostavku("passwordInbox"));
        checkEmailTask.setSubjectKeyword(konfigApp.dajPostavku("subjectKljucnaRijec"));
        checkEmailTask.setAdminEmail(konfigApp.dajPostavku("administratorEmail"));
        checkEmailTask.setAdminSubject(konfigApp.dajPostavku("administratorSubject"));
        timer.schedule(checkEmailTask, 0, Integer.parseInt(konfigApp.dajPostavku("intervalProvjereSanducica")) * 1000);
    }

    /**
     * 
     * @param sce 
     * metoda ponistava timer (Dretvu)
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        timer.cancel();
        timer.purge();
    }
}
