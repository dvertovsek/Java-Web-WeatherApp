/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.konfiguracije.bp;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.dvertovs.konfiguracije.NemaKonfiguracije;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author dare
 */
public class BP_KonfiguracijaTest {
    
    String datoteka = "NWTiS.db.config_1.xml";
    
    public BP_KonfiguracijaTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
        
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getServerDatabase method, of class BP_Konfiguracija.
     */
    @Test
    @Ignore
    public void testGetServerDatabase() {
        System.out.println("getServerDatabase");
        BP_Konfiguracija instance = null;
        String expResult = "";
        String result = instance.getServerDatabase();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAdminDatabase method, of class BP_Konfiguracija.
     */
    @Test
    @Ignore
    public void testGetAdminDatabase() {
        System.out.println("getAdminDatabase");
        BP_Konfiguracija instance = null;
        String expResult = "";
        String result = instance.getAdminDatabase();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAdminPassword method, of class BP_Konfiguracija.
     */
    @Test
    @Ignore
    public void testGetAdminPassword() {
        System.out.println("getAdminPassword");
        BP_Konfiguracija instance = null;
        String expResult = "";
        String result = instance.getAdminPassword();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getAdminUsername method, of class BP_Konfiguracija.
     */
    @Test
    public void testGetAdminUsername() {
        System.out.println("getAdminUsername");
        BP_Konfiguracija instance;
        String result = "";
        try {
            instance = new BP_Konfiguracija(datoteka);
            result = instance.getAdminUsername();
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(BP_KonfiguracijaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        String expResult = "nwtis_admin";
        
        assertEquals(expResult, result);
    }

    /**
     * Test of getDriverDatabase method, of class BP_Konfiguracija.
     */
    @Test
    public void testGetDriverDatabase_0args() {
        System.out.println("getDriverDatabase");
        BP_Konfiguracija instance;
        String result = "";
        try {
            instance = new BP_Konfiguracija(datoteka);
            result = instance.getDriverDatabase();
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(BP_KonfiguracijaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        String expResult = "com.mysql.jdbc.Driver";
        
        assertEquals(expResult, result);
    }

    /**
     * Test of getDriverDatabase method, of class BP_Konfiguracija.
     */
    @Test
    public void testGetDriverDatabase_String() {
        System.out.println("getDriverDatabase");
        BP_Konfiguracija instance;
        String result = "";
        
        String bp_url = "jdbc:mysql://192.168.15.1/";
        try {
            instance = new BP_Konfiguracija(datoteka);
            result = instance.getDriverDatabase(bp_url);
        } catch (NemaKonfiguracije ex) {
            Logger.getLogger(BP_KonfiguracijaTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        String expResult = "com.mysql.jdbc.Driver";
        
        assertEquals(expResult, result);
    }

    /**
     * Test of getDriversDatabase method, of class BP_Konfiguracija.
     */
    @Test
    @Ignore
    public void testGetDriversDatabase() {
        System.out.println("getDriversDatabase");
        BP_Konfiguracija instance = null;
        Properties expResult = null;
        Properties result = instance.getDriversDatabase();
        assertEquals(expResult, result);
    }

    /**
     * Test of getUserDatabase method, of class BP_Konfiguracija.
     */
    @Test
    @Ignore
    public void testGetUserDatabase() {
        System.out.println("getUserDatabase");
        BP_Konfiguracija instance = null;
        String expResult = "";
        String result = instance.getUserDatabase();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserPassword method, of class BP_Konfiguracija.
     */
    @Test
    @Ignore
    public void testGetUserPassword() {
        System.out.println("getUserPassword");
        BP_Konfiguracija instance = null;
        String expResult = "";
        String result = instance.getUserPassword();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getUserUsername method, of class BP_Konfiguracija.
     */
    @Test
    @Ignore
    public void testGetUserUsername() {
        System.out.println("getUserUsername");
        BP_Konfiguracija instance = null;
        String expResult = "";
        String result = instance.getUserUsername();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
