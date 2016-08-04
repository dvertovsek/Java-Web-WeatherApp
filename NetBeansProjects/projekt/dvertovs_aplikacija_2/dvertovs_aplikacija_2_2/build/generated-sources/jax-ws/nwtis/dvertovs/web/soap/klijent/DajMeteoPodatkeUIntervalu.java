
package nwtis.dvertovs.web.soap.klijent;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for dajMeteoPodatkeUIntervalu complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="dajMeteoPodatkeUIntervalu"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="auth" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="adresa" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="pocetniDatum" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="krajnjiDatum" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "dajMeteoPodatkeUIntervalu", propOrder = {
    "auth",
    "adresa",
    "pocetniDatum",
    "krajnjiDatum"
})
public class DajMeteoPodatkeUIntervalu {

    protected String auth;
    protected String adresa;
    protected String pocetniDatum;
    protected String krajnjiDatum;

    /**
     * Gets the value of the auth property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAuth() {
        return auth;
    }

    /**
     * Sets the value of the auth property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAuth(String value) {
        this.auth = value;
    }

    /**
     * Gets the value of the adresa property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAdresa() {
        return adresa;
    }

    /**
     * Sets the value of the adresa property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAdresa(String value) {
        this.adresa = value;
    }

    /**
     * Gets the value of the pocetniDatum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPocetniDatum() {
        return pocetniDatum;
    }

    /**
     * Sets the value of the pocetniDatum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPocetniDatum(String value) {
        this.pocetniDatum = value;
    }

    /**
     * Gets the value of the krajnjiDatum property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getKrajnjiDatum() {
        return krajnjiDatum;
    }

    /**
     * Sets the value of the krajnjiDatum property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setKrajnjiDatum(String value) {
        this.krajnjiDatum = value;
    }

}
