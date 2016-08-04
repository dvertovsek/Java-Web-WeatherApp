/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.rest.server;

import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.DELETE;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author dare
 */
public class App_2RESTResource {

    private String id;

    /**
     * Creates a new instance of App_2RESTResource
     */
    private App_2RESTResource(String id) {
        this.id = id;
    }

    /**
     * Get instance of the App_2RESTResource
     */
    public static App_2RESTResource getInstance(String id) {
        // The user may use some kind of persistence mechanism
        // to store and restore instances of App_2RESTResource class.
        return new App_2RESTResource(id);
    }

    /**
     * Retrieves representation of an instance of nwtis.dvertovs.rest.server.App_2RESTResource
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        //TODO return proper representation object
        throw new UnsupportedOperationException();
    }

    /**
     * PUT method for updating or creating an instance of App_2RESTResource
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }

    /**
     * DELETE method for resource App_2RESTResource
     */
    @DELETE
    public void delete() {
    }
}
