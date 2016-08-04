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
public class Brojac_2 implements Brojaci{
        
    public static int brojInstanci = 0;
    
    public int redniBroj;
    public PropertyChangeSupport podrskaZaPromjenu;
        
    public Brojac_2() {
        brojInstanci++;
        podrskaZaPromjenu = new PropertyChangeSupport(redniBroj);
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
        System.out.println("Klasa: "+this.getClass());
        Random rand = new Random();
        setRedniBroj(rand.nextInt(100));
    }
}
