/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.web;

import java.beans.PropertyChangeListener;

/**
 *
 * @author dare
 */
public interface Brojaci extends Runnable {

    void dodajSlusaca(PropertyChangeListener slusac);

    void obrisiSlusaca(PropertyChangeListener slusac);

    void setRedniBroj(int redniBroj);

    @Override
    void run();
}
