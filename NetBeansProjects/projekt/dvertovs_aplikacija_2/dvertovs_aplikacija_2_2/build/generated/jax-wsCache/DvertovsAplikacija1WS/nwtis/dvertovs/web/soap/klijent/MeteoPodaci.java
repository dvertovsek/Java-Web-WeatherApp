
package nwtis.dvertovs.web.soap.klijent;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Java class for meteoPodaci complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="meteoPodaci"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="humidityValue" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/&gt;
 *         &lt;element name="lastUpdate" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="pressureValue" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/&gt;
 *         &lt;element name="preuzeto" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="temperatureMax" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/&gt;
 *         &lt;element name="temperatureMin" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/&gt;
 *         &lt;element name="temperatureValue" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/&gt;
 *         &lt;element name="weatherValue" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="windDirectionValue" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/&gt;
 *         &lt;element name="windSpeedValue" type="{http://www.w3.org/2001/XMLSchema}float" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "meteoPodaci", propOrder = {
    "humidityValue",
    "lastUpdate",
    "pressureValue",
    "preuzeto",
    "temperatureMax",
    "temperatureMin",
    "temperatureValue",
    "weatherValue",
    "windDirectionValue",
    "windSpeedValue"
})
public class MeteoPodaci {

    protected Float humidityValue;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar lastUpdate;
    protected Float pressureValue;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar preuzeto;
    protected Float temperatureMax;
    protected Float temperatureMin;
    protected Float temperatureValue;
    protected String weatherValue;
    protected Float windDirectionValue;
    protected Float windSpeedValue;

    /**
     * Gets the value of the humidityValue property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getHumidityValue() {
        return humidityValue;
    }

    /**
     * Sets the value of the humidityValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setHumidityValue(Float value) {
        this.humidityValue = value;
    }

    /**
     * Gets the value of the lastUpdate property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getLastUpdate() {
        return lastUpdate;
    }

    /**
     * Sets the value of the lastUpdate property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setLastUpdate(XMLGregorianCalendar value) {
        this.lastUpdate = value;
    }

    /**
     * Gets the value of the pressureValue property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getPressureValue() {
        return pressureValue;
    }

    /**
     * Sets the value of the pressureValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setPressureValue(Float value) {
        this.pressureValue = value;
    }

    /**
     * Gets the value of the preuzeto property.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getPreuzeto() {
        return preuzeto;
    }

    /**
     * Sets the value of the preuzeto property.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setPreuzeto(XMLGregorianCalendar value) {
        this.preuzeto = value;
    }

    /**
     * Gets the value of the temperatureMax property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getTemperatureMax() {
        return temperatureMax;
    }

    /**
     * Sets the value of the temperatureMax property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setTemperatureMax(Float value) {
        this.temperatureMax = value;
    }

    /**
     * Gets the value of the temperatureMin property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getTemperatureMin() {
        return temperatureMin;
    }

    /**
     * Sets the value of the temperatureMin property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setTemperatureMin(Float value) {
        this.temperatureMin = value;
    }

    /**
     * Gets the value of the temperatureValue property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getTemperatureValue() {
        return temperatureValue;
    }

    /**
     * Sets the value of the temperatureValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setTemperatureValue(Float value) {
        this.temperatureValue = value;
    }

    /**
     * Gets the value of the weatherValue property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getWeatherValue() {
        return weatherValue;
    }

    /**
     * Sets the value of the weatherValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setWeatherValue(String value) {
        this.weatherValue = value;
    }

    /**
     * Gets the value of the windDirectionValue property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getWindDirectionValue() {
        return windDirectionValue;
    }

    /**
     * Sets the value of the windDirectionValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setWindDirectionValue(Float value) {
        this.windDirectionValue = value;
    }

    /**
     * Gets the value of the windSpeedValue property.
     * 
     * @return
     *     possible object is
     *     {@link Float }
     *     
     */
    public Float getWindSpeedValue() {
        return windSpeedValue;
    }

    /**
     * Sets the value of the windSpeedValue property.
     * 
     * @param value
     *     allowed object is
     *     {@link Float }
     *     
     */
    public void setWindSpeedValue(Float value) {
        this.windSpeedValue = value;
    }

}
