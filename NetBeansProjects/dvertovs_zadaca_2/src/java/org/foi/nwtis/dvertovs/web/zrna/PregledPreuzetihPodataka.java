/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.web.zrna;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Map;
import javax.inject.Named;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.ServletContext;
import org.foi.nwtis.dvertovs.konfiguracije.Konfiguracija;
import org.foi.nwtis.dvertovs.konfiguracije.KonfiguracijaApstraktna;
import org.foi.nwtis.dvertovs.konfiguracije.NemaKonfiguracije;

/**
 *
 * @author dare
 */
@Named(value = "pregledPreuzetihPodataka")
@RequestScoped
public class PregledPreuzetihPodataka {

    @Inject
    PregledDatoteke datView;

    private ArrayList<Datoteka> files;

    /**
     *
     * @return metoda koja vraca listu preuzetih datoteka (datoteka predstavlja
     * spremljenu web stranicu)
     * @throws NemaKonfiguracije
     * @throws IOException
     */
    public ArrayList<Datoteka> getDatotekaList() throws NemaKonfiguracije, IOException {
        this.files = new ArrayList<>();
        File[] fileList = getFilesList();
        if (fileList != null) {
            searchForFiles(fileList);
        }
        return files;
    }

    /**
     * 
     * @return metoda vraca popis svih fileova unutar konfiguriranog direktorija za spremanje datoteka (web stranica)
     * @throws NemaKonfiguracije
     */
    private File[] getFilesList() throws NemaKonfiguracije {
        ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
        String konfiguracija = servletContext.getInitParameter("konfiguracijaApp");
        String putanje = servletContext.getRealPath("/WEB-INF") + File.separator;
        Konfiguracija konfig = KonfiguracijaApstraktna.preuzmiKonfiguraciju(putanje + konfiguracija);
        String fullPath = putanje + konfig.dajPostavku("direktorijZaSpremanjeWebsite");
        return new File(fullPath).listFiles();
    }

    /**
     *
     * @param files lista datoteka unutar pojedinog direktorija na temelju koje ce se ispitivati radi li se o direktoriju ili datoteci
     * Ukoliko se radi o datoteci, dodaje se u listu datoteka
     * Ukoliko se radi o direktoriju, rekurzivno se poziva funkcija kako bi se naposljetku ispisale sve datoteke tog direktorija
     * @throws IOException
     */
    private void searchForFiles(File[] files) throws IOException {
        for (File file : files) {
            if (file.isDirectory()) {
                System.out.println("Directory: " + file.getName());
                searchForFiles(file.listFiles());
            } else {
                addToList(file);
            }
        }
    }

    /**
     * 
     * @param file datoteka koja ce biti dodana u listu datoteka zajedno sa svim podacima o istoj
     * @throws IOException 
     */
    private void addToList(File file) throws IOException {
        String stringPath = file.getAbsolutePath();
        Datoteka datoteka = new Datoteka();
        datoteka.setAbsPath(stringPath);
        datoteka.setName(file.getName());
        datoteka.setSize(file.length() + " B");

        Path path = Paths.get(stringPath);
        BasicFileAttributes attr = Files.readAttributes(path, BasicFileAttributes.class
        );
        FileTime fileCreated = attr.creationTime();
        datoteka.setTimeCreated(fileCreated.toString());
        this.files.add(datoteka);
    }

    /**
     * 
     * @return metoda vraca tekst o rezultatu citanja datoteke
     * Metoda takodjer prosljeduje u zrno PregledDatoteke objekt datoteke ciji ce detalji biti prikazani
     * @throws IOException
     * @throws NemaKonfiguracije 
     */
    public String pregledDatoteke() throws IOException, NemaKonfiguracije {

        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String checkString = params.get("path");

        for (Datoteka dat : getDatotekaList()) {
            if (dat.getAbsPath().equals(checkString)) {
                datView.setDatoteka(dat);
            }
        }
        if (datView.getDatoteka() != null) {
            return "OK";
        } else {
            return "ERROR";
        }
    }
}
