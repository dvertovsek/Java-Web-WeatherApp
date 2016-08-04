/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.web;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Random;

/**
 *
 * @author dare
 */
public class Brojac_1 implements Brojaci{
        
    public static int brojInstanci = 0;
    
    public int redniBroj;
    public PropertyChangeSupport podrskaZaPromjenu;
        
    public Brojac_1() {
        brojInstanci++;
        podrskaZaPromjenu = new PropertyChangeSupport(this);
    }
    
    @Override
    public void dodajSlusaca(PropertyChangeListener pct){
        podrskaZaPromjenu.addPropertyChangeListener(pct);
    }
    @Override
    public void obrisiSlusaca(PropertyChangeListener pct){
        podrskaZaPromjenu.removePropertyChangeListener(pct);
    }
    @Override
    public void setRedniBroj(int redniBroj){
        int oldValue = this.redniBroj;
        this.redniBroj = redniBroj;
        podrskaZaPromjenu.firePropertyChange("redniBroj", oldValue, redniBroj);
    }

    @Override
    public void run() {
        System.out.println("Klasa: "+this.getClass().toString());
        Random rand = new Random(System.currentTimeMillis());
        this.setRedniBroj(rand.nextInt(100));
    }
}
