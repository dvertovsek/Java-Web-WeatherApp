/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.web.zrna;

/**
 *
 * @author dare
 */
public class Polaznici {
    private String korisnik;
    private String ime;
    private String prezime;
    private String lozinka;
    private String email;
    private int vrsta;
    private String datumKreiranja;
    private String datumPromjene;

    public String getKorisnik() {
        return korisnik;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getLozinka() {
        return lozinka;
    }

    public String getEmail() {
        return email;
    }

    public int getVrsta() {
        return vrsta;
    }

    public String getDatumKreiranja() {
        return datumKreiranja;
    }

    public String getDatumPromjene() {
        return datumPromjene;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public void setLozinka(String lozinka) {
        this.lozinka = lozinka;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setVrsta(int vrsta) {
        this.vrsta = vrsta;
    }

    public void setDatumKreiranja(String datumKreiranja) {
        this.datumKreiranja = datumKreiranja;
    }

    public void setDatumPromjene(String datumPromjene) {
        this.datumPromjene = datumPromjene;
    }
}
