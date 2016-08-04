/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.web.zrna;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;

/**
 *
 * @author dare
 */
@Named(value = "pregledDatoteke")
@RequestScoped
public class PregledDatoteke {

    private Datoteka datoteka;
    private String content;

    public PregledDatoteke() {
    }

    public void setDatoteka(Datoteka datoteka) {
        this.datoteka = datoteka;
    }

    public Datoteka getDatoteka() {
        return datoteka;
    }

    /**
     * 
     * @return metoda vraca sadrzaj datoteke
     * @throws FileNotFoundException 
     */
    public String getContent() throws FileNotFoundException {
        Scanner sc = new Scanner(new File(datoteka.getAbsPath()));
        while (sc.hasNextLine()) {
            this.content += sc.nextLine();
        }
        return this.content;
    }
}
