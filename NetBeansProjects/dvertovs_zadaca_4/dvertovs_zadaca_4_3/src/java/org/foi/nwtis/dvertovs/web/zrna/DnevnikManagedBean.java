/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import org.foi.nwtis.dvertovs.ejb.eb.Dnevnik;
import org.foi.nwtis.dvertovs.ejb.sb.DnevnikFacade;

/**
 *
 * @author dare
 */
@Named(value = "dnevnikManagedBean")
@SessionScoped
public class DnevnikManagedBean implements Serializable {

    @EJB
    private DnevnikFacade dnevnikFacade;

    private List<Dnevnik> listaDnevnika;
    
    /**
     * Creates a new instance of DnevnikManagedBean
     */
    public DnevnikManagedBean() {
    }

    public List<Dnevnik> getListaDnevnika() {
        this.listaDnevnika = dnevnikFacade.findAll();
        return listaDnevnika;
    }

    public void setListaDnevnika(List<Dnevnik> listaDnevnika) {
        this.listaDnevnika = listaDnevnika;
    }
    
}
