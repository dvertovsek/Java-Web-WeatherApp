/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.web.podaci;

import java.util.Date;

/**
 *
 * @author dvertovs
 */
public class MeteoPodaci {

    private Float temperatureValue;
    private Float temperatureMin;
    private Float temperatureMax;

    private Float humidityValue;
    private Float pressureValue;
    private Float windSpeedValue;
    private Float windDirectionValue;

    private String weatherValue;
    private Date lastUpdate;
    private Date preuzeto;

    public MeteoPodaci() {
    }

    public MeteoPodaci(Float temperatureValue, Float temperatureMin, Float temperatureMax, Float humidityValue, Float pressureValue, Float windSpeedValue, Float windDirectionValue, String visibility, String weatherValue, Date lastUpdate, Date preuzeto) {
        this.temperatureValue = temperatureValue;
        this.temperatureMin = temperatureMin;
        this.temperatureMax = temperatureMax;
        this.humidityValue = humidityValue;
        this.pressureValue = pressureValue;
        this.windSpeedValue = windSpeedValue;
        this.windDirectionValue = windDirectionValue;
        this.weatherValue = weatherValue;
        this.lastUpdate = lastUpdate;
        this.preuzeto = preuzeto;
    }

    public Float getTemperatureValue() {
        return temperatureValue;
    }

    public void setTemperatureValue(Float temperatureValue) {
        this.temperatureValue = temperatureValue;
    }

    public Float getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(Float temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public Float getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(Float temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public Float getHumidityValue() {
        return humidityValue;
    }

    public void setHumidityValue(Float humidityValue) {
        this.humidityValue = humidityValue;
    }

    public Float getPressureValue() {
        return pressureValue;
    }

    public void setPressureValue(Float pressureValue) {
        this.pressureValue = pressureValue;
    }

    public Float getWindSpeedValue() {
        return windSpeedValue;
    }

    public void setWindSpeedValue(Float windSpeedValue) {
        this.windSpeedValue = windSpeedValue;
    }

    public Float getWindDirectionValue() {
        return windDirectionValue;
    }

    public void setWindDirectionValue(Float windDirectionValue) {
        this.windDirectionValue = windDirectionValue;
    }

    public String getWeatherValue() {
        return weatherValue;
    }

    public void setWeatherValue(String weatherValue) {
        this.weatherValue = weatherValue;
    }

    public Date getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(Date lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public Date getPreuzeto() {
        return preuzeto;
    }

    public void setPreuzeto(Date preuzeto) {
        this.preuzeto = preuzeto;
    }

}
