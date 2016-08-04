/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.rest.serveri;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.MediaType;
import org.foi.nwtis.dvertovs.db.DBController;

/**
 * REST Web Service
 *
 * @author dare
 */
public class MeteoRESTResource {

    private String id;

    /**
     * Creates a new instance of MeteoRESTResource
     */
    private MeteoRESTResource(String id) {
        this.id = id;
    }

    /**
     * Get instance of the MeteoRESTResource
     */
    public static MeteoRESTResource getInstance(String id) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of MeteoRESTResource class.
        return new MeteoRESTResource(id);
    }

    /**
     * Retrieves representation of an instance of
     * org.foi.nwtis.dvertovs.rest.serveri.MeteoRESTResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {

        ResultSet rsAddr = null;
        JsonObjectBuilder jbf = Json.createObjectBuilder();
        JsonArrayBuilder meteoArray = Json.createArrayBuilder();
        boolean added = false;
        try {
            rsAddr = DBController.getDbCon().getMeteoInfoForAddress(Integer.parseInt(id));
            String resultAddr = "";
            while (rsAddr.next()) {
                JsonObjectBuilder meteo = Json.createObjectBuilder();
                meteo.add("vrijeme", rsAddr.getInt(1));
                meteo.add("vrijemeopis", rsAddr.getString(2));
                meteo.add("temp", rsAddr.getFloat(3));
                meteo.add("tempmin", rsAddr.getFloat(4));
                meteo.add("tempmax", rsAddr.getFloat(5));
                meteo.add("vlaga", rsAddr.getFloat(6));
                meteo.add("tlak", rsAddr.getFloat(7));
                meteo.add("vjetar", rsAddr.getFloat(8));
                meteo.add("vjetarsmjer", rsAddr.getFloat(9));
                meteo.add("preuzeto", rsAddr.getString(10));
                meteoArray.add(meteo);
                resultAddr = rsAddr.getString(11);
                added = true;
            }
            jbf.add("adresa", resultAddr);
            jbf.add("meteo", meteoArray);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | SQLException ex) {
            Logger.getLogger(MeteoRESTResourceContainer.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (added) {
            return jbf.build().toString();
        } else {
            return "";
        }
    }

    /**
     * PUT method for updating or creating an instance of MeteoRESTResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }

    /**
     * DELETE method for resource MeteoRESTResource
     */
    @DELETE
    public void delete() {
    }
}
