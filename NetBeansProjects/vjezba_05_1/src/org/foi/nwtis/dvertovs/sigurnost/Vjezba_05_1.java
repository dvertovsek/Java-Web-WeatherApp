/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.sigurnost;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.foi.nwtis.dvertovs.konfiguracije.*;
/**
 *
 * @author dare
 */
public class Vjezba_05_1 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        if(args.length != 1)
        {
            System.exit(0);
        }
        else
        {
            File f = new File(args[0]);
            if(!f.exists())
            {
                System.out.println("Greska! Fajl ne postoji!");
                System.exit(0);
            }
            else
            {
                System.out.println("argument: " + args[0]);
                try {
                    Konfiguracija konf = KonfiguracijaApstraktna.preuzmiKonfiguraciju(args[0]);
                    System.out.println(konf.dajSvePostavke());
                } catch (NemaKonfiguracije ex) {
                    Logger.getLogger(Vjezba_05_1.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        }
    }
    
}
