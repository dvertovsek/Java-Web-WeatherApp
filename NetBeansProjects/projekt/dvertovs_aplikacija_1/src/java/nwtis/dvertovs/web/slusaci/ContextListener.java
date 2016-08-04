/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.web.slusaci;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import nwtis.dvertovs.db.DBController;
import nwtis.dvertovs.web.MeteoDataCollectorTask;
import nwtis.dvertovs.web.UserRequestsResetTask;
import nwtis.dvertovs.web.socketserver.ServerSustava;
import org.foi.nwtis.dvertovs.konfiguracije.Konfiguracija;
import org.foi.nwtis.dvertovs.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.dvertovs.konfiguracije.NemaKonfiguracije;
import org.foi.nwtis.dvertovs.konfiguracije.bp.BP_Konfiguracija;

/**
 * Web application lifecycle listener.
 *
 * @author dare
 */
public class ContextListener implements ServletContextListener {

    ServletContext servletContext;
    private Timer timer;
    private Timer resetRequestsTimer;
    private ServerSustava serverSustava;

    /**
     * metoda cita iz konfiguracije naziv konfiguracije za bazu podataka i za
     * aplikaciju te ucitava konfiguraciju Konfiguracija se sprema u atribut
     * konteksta postavljaju se varijable klase singleton za pristup bazi
     * podataka inicijalizira se varijabla timera te pokrece klasa TimerTask
     * koja u intervalu preuzima meteo podatke za adrese u bazi
     *
     * @param sce
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        this.servletContext = sce.getServletContext();
        String konfiguracijaDB = servletContext.getInitParameter("konfiguracija");
        String konfiguracijaApp = servletContext.getInitParameter("konfiguracijaApp");
        String putanje = servletContext.getRealPath("/WEB-INF") + File.separator;

        BP_Konfiguracija konfigDB = null;
        Konfiguracija konfigApp = null;
        try {
            konfigDB = new BP_Konfiguracija(putanje + konfiguracijaDB);
            konfigApp = KonfiguracijaApstraktna.preuzmiKonfiguraciju(putanje + konfiguracijaApp);
            servletContext.setAttribute("BP_Konfig", konfigDB);
            servletContext.setAttribute("App_Konfig", konfigApp);
            System.out.println("Ucitana konfiguracija.");

            DBController.DatabaseURL = konfigDB.getServerDatabase() + konfigDB.getUserDatabase();
            DBController.DatabaseDriver = konfigDB.getDriverDatabase();
            DBController.DatabaseUser = konfigDB.getUserUsername();
            DBController.DatabasePass = konfigDB.getUserPassword();

        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(ContextListener.class.getName()).log(Level.SEVERE, null, ex);
        }

        timer = new Timer();
        MeteoDataCollectorTask meteoDataCollectorTask = new MeteoDataCollectorTask(konfigApp.dajPostavku("OWMAppID"));
        timer.schedule(meteoDataCollectorTask, 0, Integer.parseInt(konfigApp.dajPostavku("intervalPreuzimanjaMeteo")) * 1000);

        resetRequestsTimer = new Timer();
        UserRequestsResetTask userRequestsResetTask = new UserRequestsResetTask();
        resetRequestsTimer.schedule(userRequestsResetTask, 0, Integer.parseInt(konfigApp.dajPostavku("intervalDozvoljenKorisnicima")) * 1000);

        serverSustava = new ServerSustava(konfigApp, meteoDataCollectorTask);
        serverSustava.start();
    }

    /**
     *
     * @param sce metoda ponistava timer (Dretvu)
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        timer.cancel();
        timer.purge();
        resetRequestsTimer.cancel();
        resetRequestsTimer.purge();
        
        serverSustava.end();
    }

    public ServletContext getServletContext() {
        return servletContext;
    }
}
