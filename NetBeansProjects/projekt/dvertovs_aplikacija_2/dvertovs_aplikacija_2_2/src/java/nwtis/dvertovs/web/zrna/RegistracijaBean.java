/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.web.zrna;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import javax.ejb.EJB;
import nwtis.dvertovs.ejb.eb.DvertovsUsers;
import nwtis.dvertovs.ejb.sb.DvertovsUsersFacade;

/**
 *
 * @author dare
 */
@Named(value = "registracijaBean")
@SessionScoped
public class RegistracijaBean implements Serializable {

    @EJB
    private DvertovsUsersFacade dvertovsUsersFacade;

    private String message = "";

    private String username;
    private String password;
    private String passwordRepeat;
    private String type;

    /**
     * Creates a new instance of prijavaRegistracijaBean
     */
    public RegistracijaBean() {
    }

    public void register() {
        if (type == null || this.username.equals("") || !password.equals(passwordRepeat)) {
            message = "ERROR";
        } else {
            boolean admin = false;
            if(type.equals("ADMIN")){
                admin = true;
            }
            DvertovsUsers dv = new DvertovsUsers(Integer.BYTES, username, password, admin, 0);
            dvertovsUsersFacade.create(dv);
            message = "OK";
        }
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

    public String getPasswordRepeat() {
        return passwordRepeat;
    }

    public void setPasswordRepeat(String passwordRepeat) {
        this.passwordRepeat = passwordRepeat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
