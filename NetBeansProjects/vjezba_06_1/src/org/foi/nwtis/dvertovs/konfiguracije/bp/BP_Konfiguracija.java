/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.konfiguracije.bp;

import java.util.Properties;
import org.foi.nwtis.dvertovs.konfiguracije.Konfiguracija;
import org.foi.nwtis.dvertovs.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.dvertovs.konfiguracije.NemaKonfiguracije;

/**
 *
 * @author dare
 */
public class BP_Konfiguracija implements BP_Sucelje {

    Konfiguracija konfig;
    
    public BP_Konfiguracija(String datoteka) throws NemaKonfiguracije {
        
        konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(datoteka);
    }

    @Override
    public String getServerDatabase() {
        return konfig.dajPostavku("server.database");
    }
    
    @Override
    public String getAdminDatabase() {
        return konfig.dajPostavku("admin.database");
    }

    @Override
    public String getAdminPassword() {
        return konfig.dajPostavku("admin.password");
    }

    @Override
    public String getAdminUsername() {
        return konfig.dajPostavku("admin.username");
    }

    @Override
    public String getDriverDatabase() {
        String[] arr = this.getServerDatabase().split(":");
        return konfig.dajPostavku("driver.database."+arr[1]);
    }

    @Override
    public String getDriverDatabase(String bp_url) {
        String[] arr = bp_url.split(":");
        return konfig.dajPostavku("driver.database."+arr[1]);
    }

    @Override
    public Properties getDriversDatabase() {
        Properties properties = new Properties();
        properties.setProperty("driver.database.odbc", konfig.dajPostavku("driver.database.odbc"));
        properties.setProperty("driver.database.mysql", konfig.dajPostavku("driver.database.mysql"));
        properties.setProperty("driver.database.derby", konfig.dajPostavku("driver.database.derby"));
        properties.setProperty("driver.database.postgresql", konfig.dajPostavku("driver.database.postgresql"));
        return properties;
    }

    @Override
    public String getUserDatabase() {
        return konfig.dajPostavku("user.database");
    }

    @Override
    public String getUserPassword() {
        return konfig.dajPostavku("user.password");
    }

    @Override
    public String getUserUsername() {
        return konfig.dajPostavku("user.username");
    }
}
