/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.web.slusaci;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import org.foi.nwtis.dvertovs.konfiguracije.Konfiguracija;
import org.foi.nwtis.dvertovs.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.dvertovs.konfiguracije.NemaKonfiguracije;

/**
 * Web application lifecycle listener.
 *
 * @author dare
 */
public class ContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();

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
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
