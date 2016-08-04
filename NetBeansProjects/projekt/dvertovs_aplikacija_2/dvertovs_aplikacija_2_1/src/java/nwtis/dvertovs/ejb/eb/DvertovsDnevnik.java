/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.ejb.eb;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author dare
 */
@Entity
@Table(name = "DVERTOVS_DNEVNIK")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DvertovsDnevnik.findAll", query = "SELECT d FROM DvertovsDnevnik d"),
    @NamedQuery(name = "DvertovsDnevnik.findByIduser", query = "SELECT d FROM DvertovsDnevnik d WHERE d.iduser = :iduser"),
    @NamedQuery(name = "DvertovsDnevnik.findByKorisnik", query = "SELECT d FROM DvertovsDnevnik d WHERE d.korisnik = :korisnik"),
    @NamedQuery(name = "DvertovsDnevnik.findByAkcija", query = "SELECT d FROM DvertovsDnevnik d WHERE d.akcija = :akcija"),
    @NamedQuery(name = "DvertovsDnevnik.findByVrijeme", query = "SELECT d FROM DvertovsDnevnik d WHERE d.vrijeme = :vrijeme")})
public class DvertovsDnevnik implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDUSER")
    private Integer iduser;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 25)
    @Column(name = "KORISNIK")
    private String korisnik;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "AKCIJA")
    private String akcija;
    @Column(name = "VRIJEME")
    @Temporal(TemporalType.TIMESTAMP)
    private Date vrijeme;

    public DvertovsDnevnik() {
    }

    public DvertovsDnevnik(Integer iduser) {
        this.iduser = iduser;
    }

    public DvertovsDnevnik(Integer iduser, String korisnik, String akcija) {
        this.iduser = iduser;
        this.korisnik = korisnik;
        this.akcija = akcija;
    }

    public Integer getIduser() {
        return iduser;
    }

    public void setIduser(Integer iduser) {
        this.iduser = iduser;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public void setKorisnik(String korisnik) {
        this.korisnik = korisnik;
    }

    public String getAkcija() {
        return akcija;
    }

    public void setAkcija(String akcija) {
        this.akcija = akcija;
    }

    public Date getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(Date vrijeme) {
        this.vrijeme = vrijeme;
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
        if (!(object instanceof DvertovsDnevnik)) {
            return false;
        }
        DvertovsDnevnik other = (DvertovsDnevnik) object;
        if ((this.iduser == null && other.iduser != null) || (this.iduser != null && !this.iduser.equals(other.iduser))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "nwtis.dvertovs.ejb.eb.DvertovsDnevnik[ iduser=" + iduser + " ]";
    }
    
}
