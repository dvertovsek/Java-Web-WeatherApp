/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.web;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author dare
 */
public class DretvaPromjena extends Thread {

    SlusacPromjena sp;
    int timeInterval;
    String klasaNaziv;

    Brojaci brojac;

    public DretvaPromjena(SlusacPromjena sp, int timeInterval, String klasa) {
        this.sp = sp;
        this.timeInterval = timeInterval;
        this.klasaNaziv = klasa;
    }

    @Override
    public synchronized void start() {
        try {
            Class klasa = Class.forName(this.klasaNaziv);
            this.brojac = (Brojaci) klasa.newInstance();
            this.brojac.dodajSlusaca(sp);
            super.start();
        } catch (InstantiationException ex) {
            Logger.getLogger(DretvaPromjena.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(DretvaPromjena.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(DretvaPromjena.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @Override
    public void run() {
        while (true) { 
            try {
                this.brojac.run();
                sleep(timeInterval * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(DretvaPromjena.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
