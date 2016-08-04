/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.konfiguracije.bp;

import java.util.Properties;

/**
 *
 * @author dare
 */
public interface BP_Sucelje {

    String getAdminDatabase();

    String getAdminPassword();

    String getAdminUsername();

    String getDriverDatabase();

    String getDriverDatabase(String bp_url);

    Properties getDriversDatabase();

    String getServerDatabase();

    String getUserDatabase();

    String getUserPassword();

    String getUserUsername();
}
