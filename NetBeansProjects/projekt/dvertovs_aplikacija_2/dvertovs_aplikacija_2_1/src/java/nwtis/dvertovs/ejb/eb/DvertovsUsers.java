/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.ejb.eb;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author dare
 */
@Entity
@Table(name = "DVERTOVS_USERS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DvertovsUsers.findAll", query = "SELECT d FROM DvertovsUsers d"),
    @NamedQuery(name = "DvertovsUsers.findByIduser", query = "SELECT d FROM DvertovsUsers d WHERE d.iduser = :iduser"),
    @NamedQuery(name = "DvertovsUsers.findByUsername", query = "SELECT d FROM DvertovsUsers d WHERE d.username = :username"),
    @NamedQuery(name = "DvertovsUsers.findByPassword", query = "SELECT d FROM DvertovsUsers d WHERE d.password = :password"),
    @NamedQuery(name = "DvertovsUsers.findByIsadmin", query = "SELECT d FROM DvertovsUsers d WHERE d.isadmin = :isadmin"),
    @NamedQuery(name = "DvertovsUsers.findByIsaccepted", query = "SELECT d FROM DvertovsUsers d WHERE d.isaccepted = :isaccepted")})
public class DvertovsUsers implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDUSER")
    private Integer iduser;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "USERNAME")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 50)
    @Column(name = "PASSWORD")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ISADMIN")
    private Boolean isadmin;
    @Basic(optional = false)
    @NotNull
    @Column(name = "ISACCEPTED")
    private int isaccepted;

    public DvertovsUsers() {
    }

    public DvertovsUsers(Integer iduser) {
        this.iduser = iduser;
    }

    public DvertovsUsers(Integer iduser, String username, String password, Boolean isadmin, int isaccepted) {
        this.iduser = iduser;
        this.username = username;
        this.password = password;
        this.isadmin = isadmin;
        this.isaccepted = isaccepted;
    }

    public Integer getIduser() {
        return iduser;
    }

    public void setIduser(Integer iduser) {
        this.iduser = iduser;
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

    public Boolean getIsadmin() {
        return isadmin;
    }

    public void setIsadmin(Boolean isadmin) {
        this.isadmin = isadmin;
    }

    public int getIsaccepted() {
        return isaccepted;
    }

    public void setIsaccepted(int isaccepted) {
        this.isaccepted = isaccepted;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iduser != null ? iduser.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DvertovsUsers)) {
            return false;
        }
        DvertovsUsers other = (DvertovsUsers) object;
        if ((this.iduser == null && other.iduser != null) || (this.iduser != null && !this.iduser.equals(other.iduser))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "nwtis.dvertovs.ejb.eb.DvertovsUsers[ iduser=" + iduser + " ]";
    }
    
}
