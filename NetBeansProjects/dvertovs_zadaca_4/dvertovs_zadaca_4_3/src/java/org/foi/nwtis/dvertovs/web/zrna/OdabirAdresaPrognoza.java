/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import org.foi.nwtis.dvertovs.ejb.eb.Adrese;
import org.foi.nwtis.dvertovs.ejb.eb.Dnevnik;
import org.foi.nwtis.dvertovs.ejb.sb.AdreseFacade;
import org.foi.nwtis.dvertovs.ejb.sb.DnevnikFacade;
import org.foi.nwtis.dvertovs.ejb.sb.MeteoAdresniKlijent;
import org.foi.nwtis.dvertovs.konfiguracije.Konfiguracija;
import org.foi.nwtis.dvertovs.web.podaci.Adresa;
import org.foi.nwtis.dvertovs.web.podaci.Lokacija;
import org.foi.nwtis.dvertovs.web.podaci.MeteoPrognoza;

/**
 *
 * @author dare
 */
@Named(value = "odabirAdresaPrognoza")
@SessionScoped
public class OdabirAdresaPrognoza implements Serializable {

    @EJB
    private DnevnikFacade dnevnikFacade;

    @EJB
    private MeteoAdresniKlijent meteoAdresniKlijent;

    @EJB
    private AdreseFacade adreseFacade;
    
    private String novaAdresa;
    private boolean novaAdresaIsAdded;

    private Map<String, Adresa> popisAdresa;

    private Map<String, Object> popisAktivnihAdresa;
    private List<String> listaOdabranihAdresaZaDodavanje;

    private Map<String, Object> popisKandidiranihAdresa;
    private List<String> listaOdabranihAdresaZaBrisanje;

    private String azuriranaAdresa;
    private String idAzuriranaAdresa;

    private List<MeteoPrognoza> listaMeteoPrognoza;

    private String prikazAzuriranjaAdrese;
    private String prikazPregledaPrognozaAdrese;

    /**
     * Creates a new instance of OdabirAdresaPrognoza
     */
    public OdabirAdresaPrognoza() {
        this.prikazAzuriranjaAdrese = "hidden";
        this.prikazPregledaPrognozaAdrese = "hidden";
    }

    public void preuzmi() {
        if (this.popisKandidiranihAdresa == null) {
            this.popisKandidiranihAdresa = new TreeMap<>();
        }
        for (String a : this.listaOdabranihAdresaZaDodavanje) {
            this.popisAktivnihAdresa.remove(this.popisAdresa.get(a).getAdresa());
            this.popisKandidiranihAdresa.put(this.popisAdresa.get(a).getAdresa(), a);
        }
        dnevnikFacade.create(createDnevnikEntry("PREUZMI"));
    }

    public void makni() {
        for (String a : this.listaOdabranihAdresaZaBrisanje) {
            String adresaNaziv = this.popisAdresa.get(a).getAdresa();
            this.popisKandidiranihAdresa.remove(adresaNaziv);
            this.popisAktivnihAdresa.put(adresaNaziv, a);
        }
        dnevnikFacade.create(createDnevnikEntry("MAKNI"));
    }

    public void azuriraj() {
        this.idAzuriranaAdresa = this.listaOdabranihAdresaZaDodavanje.get(0);
        this.azuriranaAdresa = this.popisAdresa.get(this.idAzuriranaAdresa).getAdresa();
        this.prikazAzuriranjaAdrese = "visible";
        dnevnikFacade.create(createDnevnikEntry("AZURIRAJ"));
    }

    public void dodajNovuAdresu() {
        Lokacija l = meteoAdresniKlijent.dajLokaciju(this.novaAdresa);
        Adrese dodanaAdresa = new Adrese(Integer.BYTES, novaAdresa, l.getLatitude(), l.getLongitude());
        adreseFacade.create(dodanaAdresa);
        dnevnikFacade.create(createDnevnikEntry("DODAJ"));
    }

    public void azurirajAdresu() {
        Adrese a = adreseFacade.find(Integer.parseInt(this.idAzuriranaAdresa));
        a.setAdresa(this.azuriranaAdresa);
        adreseFacade.edit(a);
        this.novaAdresaIsAdded = true;
        this.prikazAzuriranjaAdrese = "hidden";
        dnevnikFacade.create(createDnevnikEntry("AZURIRAJ_ADRESU"));
    }

    public void pregledPrognoza() {
        this.prikazPregledaPrognozaAdrese = "visible";
        dnevnikFacade.create(createDnevnikEntry("PREGLED_PROGNOZA"));
    }

    public void hideMeteo() {
        this.prikazPregledaPrognozaAdrese = "hidden";
        dnevnikFacade.create(createDnevnikEntry("HIDE_METEO"));
    }

    /*
    * GETTERS AND SETTERS
     */
    public String getNovaAdresa() {
        return novaAdresa;
    }

    public void setNovaAdresa(String novaAdresa) {
        this.novaAdresaIsAdded = true;
        this.novaAdresa = novaAdresa;
    }

    public Map<String, Object> getPopisAktivnihAdresa() {
        if (this.popisAktivnihAdresa == null || this.novaAdresaIsAdded) {
            List<Adrese> adrese = adreseFacade.findAll();
            this.popisAktivnihAdresa = new TreeMap<>();
            this.popisAdresa = new TreeMap<>();
            for (Adrese a : adrese) {
                this.popisAktivnihAdresa.put(a.getAdresa(), a.getIdadresa().toString());
                this.popisAdresa.put(a.getIdadresa().toString(), new Adresa(0, a.getAdresa(), new Lokacija(a.getLatitude(), a.getLongitude())));
            }
        }
        this.novaAdresaIsAdded = false;
        dnevnikFacade.create(createDnevnikEntry("GET_AKTIVNE_ADRESE"));
        return popisAktivnihAdresa;
    }

    public void setPopisAktivnihAdresa(Map<String, Object> popisAktivnihAdresa) {
        this.popisAktivnihAdresa = popisAktivnihAdresa;
    }

    public List<MeteoPrognoza> getListaMeteoPrognoza() {
        if (this.listaOdabranihAdresaZaBrisanje != null) {
            String adrId = this.listaOdabranihAdresaZaBrisanje.get(0);
            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
            String apiKey = ((Konfiguracija) servletContext.getAttribute("App_Konfig")).dajPostavku("OWMAppID");
            this.meteoAdresniKlijent.postaviKorisnickePodatke(apiKey);
            this.listaMeteoPrognoza = meteoAdresniKlijent.dajMeteoPrognoze(this.popisAdresa.get(adrId).getAdresa());
        }
        dnevnikFacade.create(createDnevnikEntry("GET_METEO"));
        return listaMeteoPrognoza;
    }

    public void setListaMeteoPrognoza(List<MeteoPrognoza> listaMeteoPrognoza) {
        this.listaMeteoPrognoza = listaMeteoPrognoza;
    }

    public Dnevnik createDnevnikEntry(String action){
        Dnevnik dn = new Dnevnik();
        dn.setId(Integer.BYTES);
        dn.setIpadresa("localhost");
        dn.setKorisnik("user");
        dn.setStatus(0);
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        dn.setUrl(request.getRequestURI()+"/"+action);
        dn.setVrijeme(new Date());
        return dn;
    }
    
    public List<String> getListaOdabranihAdresaZaDodavanje() {
        return listaOdabranihAdresaZaDodavanje;
    }

    public void setListaOdabranihAdresaZaDodavanje(List<String> listaOdabranihAdresaZaDodavanje) {
        this.listaOdabranihAdresaZaDodavanje = listaOdabranihAdresaZaDodavanje;
    }

    public Map<String, Object> getPopisKandidiranihAdresa() {
        return popisKandidiranihAdresa;
    }

    public void setPopisKandidiranihAdresa(Map<String, Object> popisKandidiranihAdresa) {
        this.popisKandidiranihAdresa = popisKandidiranihAdresa;
    }

    public List<String> getListaOdabranihAdresaZaBrisanje() {
        return listaOdabranihAdresaZaBrisanje;
    }

    public void setListaOdabranihAdresaZaBrisanje(List<String> listaOdabranihAdresaZaBrisanje) {
        this.listaOdabranihAdresaZaBrisanje = listaOdabranihAdresaZaBrisanje;
    }

    public String getAzuriranaAdresa() {
        return azuriranaAdresa;
    }

    public void setAzuriranaAdresa(String azuriranaAdresa) {
        this.azuriranaAdresa = azuriranaAdresa;
    }

    public String getIdAzuriranaAdresa() {
        return idAzuriranaAdresa;
    }

    public void setIdAzuriranaAdresa(String idAzuriranaAdresa) {
        this.idAzuriranaAdresa = idAzuriranaAdresa;
    }

    public String getPrikazAzuriranjaAdrese() {
        return prikazAzuriranjaAdrese;
    }

    public void setPrikazAzuriranjaAdrese(String prikazAzuriranjaAdrese) {
        this.prikazAzuriranjaAdrese = prikazAzuriranjaAdrese;
    }

    public String getPrikazPregledaPrognozaAdrese() {
        return prikazPregledaPrognozaAdrese;
    }

    public void setPrikazPregledaPrognozaAdrese(String prikazPregledaPrognozaAdrese) {
        this.prikazPregledaPrognozaAdrese = prikazPregledaPrognozaAdrese;
    }
}
