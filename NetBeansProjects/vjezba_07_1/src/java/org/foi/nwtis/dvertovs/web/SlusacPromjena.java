/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.web;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 *
 * @author dare
 */
public class SlusacPromjena implements PropertyChangeListener {

    public int brojPromjena = 0;
    
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        brojPromjena++;
        
        System.out.println("Broj promjena "+this.brojPromjena);
        System.out.println("Varijabla koja je promjenila sadrzaj: "+evt.getPropertyName());
        System.out.println("Stara vrijednost: "+evt.getNewValue()+", nova vrijednost: "+evt.getOldValue());
    }
    
}
