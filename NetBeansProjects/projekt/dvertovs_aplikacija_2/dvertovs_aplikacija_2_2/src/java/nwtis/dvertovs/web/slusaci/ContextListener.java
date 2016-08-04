/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.web.slusaci;

import java.io.File;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import nwtis.dvertovs.web.CheckMailboxTask;
import org.foi.nwtis.dvertovs.konfiguracije.Konfiguracija;
import org.foi.nwtis.dvertovs.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.dvertovs.konfiguracije.NemaKonfiguracije;

/**
 * Web application lifecycle listener.
 *
 * @author dare
 */
public class ContextListener implements ServletContextListener {

    ServletContext servletContext;
    private Timer timer;

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
        String konfiguracijaApp = servletContext.getInitParameter("konfiguracijaApp");
        String putanje = servletContext.getRealPath("/WEB-INF") + File.separator;

        Konfiguracija konfigApp = null;
        try {
            konfigApp = KonfiguracijaApstraktna.preuzmiKonfiguraciju(putanje + konfiguracijaApp);
            servletContext.setAttribute("App_Konfig", konfigApp);
            System.out.println("Ucitana konfiguracija.");

        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(ContextListener.class.getName()).log(Level.SEVERE, null, ex);
        }

        timer = new Timer();
        CheckMailboxTask checkMailboxTask = new CheckMailboxTask();
        
        checkMailboxTask.setAddress(konfigApp.dajPostavku("mailserver"));
        checkMailboxTask.setIMAPPort(konfigApp.dajPostavku("IMAPport"));
        checkMailboxTask.setUser(konfigApp.dajPostavku("mailuser"));
        checkMailboxTask.setPass(konfigApp.dajPostavku("mailpass"));
        checkMailboxTask.setFolderUspjesne(konfigApp.dajPostavku("folderuspjesne"));
        checkMailboxTask.setFolderNeuspjesne(konfigApp.dajPostavku("folderneuspjesne"));
        checkMailboxTask.setFolderNeispravne(konfigApp.dajPostavku("folderneispravne"));
        
        checkMailboxTask.setSubjectKeyword(konfigApp.dajPostavku("nwtisporuka_predmet"));
        timer.schedule(checkMailboxTask, 0, Integer.parseInt(konfigApp.dajPostavku("intervalProvjereSanducica")) * 1000);

    }

    /**
     *
     * @param sce metoda ponistava timer (Dretvu)
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        timer.cancel();
        timer.purge();
    }

    public ServletContext getServletContext() {
        return servletContext;
    }
}
