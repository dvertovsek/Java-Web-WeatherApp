/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.web.zrna;

import javax.inject.Named;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.persistence.NoResultException;
import nwtis.dvertovs.ejb.eb.DvertovsUsers;
import nwtis.dvertovs.ejb.sb.DvertovsUsersFacade;

/**
 *
 * @author dare
 */
@Named(value = "prijavaBean")
@RequestScoped
public class PrijavaBean implements Serializable {

    @EJB
    private DvertovsUsersFacade dvertovsUsersFacade;

    private String message;

    private String username;
    private String password;

    /**
     * Creates a new instance of prijavaRegistracijaBean
     */
    public PrijavaBean() {
    }

    public String login() {
        DvertovsUsers dvertovsUsers;
        FacesContext context = FacesContext.getCurrentInstance();
        try {
            dvertovsUsers = dvertovsUsersFacade.getByUsername(this.username);
        } catch (EJBException | NoResultException ex) {
            message = "ERROR";
            return "ERROR";
        }
        if (dvertovsUsers.getIsaccepted() == 0) {
            message = "jos nije prihvacen";
            return "ERROR";
        } else if (dvertovsUsers.getIsaccepted() == 1) {
            message = "odbijen";
            return "ERROR";
        }
        context.getExternalContext().getSessionMap().put("user", dvertovsUsers);
        if(dvertovsUsers.getIsadmin()){
            return "OK_ADMIN";
        }
        return "OK";
    }

    public String logout() {
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
        return "LOGOUT";
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
