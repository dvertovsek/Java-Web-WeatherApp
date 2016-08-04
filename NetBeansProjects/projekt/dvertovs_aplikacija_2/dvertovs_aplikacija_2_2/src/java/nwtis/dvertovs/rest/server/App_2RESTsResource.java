/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.rest.server;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nwtis.dvertovs.ejb.eb.DvertovsUsers;

/**
 * REST Web Service
 *
 * @author dare
 */
@Path("/as")
public class App_2RESTsResource {

    @Context
    private ServletContext servletContext;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of App_2RESTsResource
     */
    public App_2RESTsResource() {
    }

    /**
     * Retrieves representation of an instance of
     * nwtis.dvertovs.rest.server.App_2RESTsResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {

        List<DvertovsUsers> listakor = (List<DvertovsUsers>) servletContext.getAttribute("KORISNICI");
        String response = "<?xml version=\"1.0\"?>";
        response += "<korisnicionline>";
        if (listakor != null) {
            for (DvertovsUsers dv : listakor) {
                response += "<korisnik>" + dv.getUsername() + "</korisnik>";
            }
        }
        response += "</korisnicionline>";
        return response;
    }

    /**
     * POST method for creating an instance of App_2RESTResource
     *
     * @param content representation for the new resource
     * @return an HTTP response with content of the created resource
     */
    @POST
    @Consumes(MediaType.APPLICATION_XML)
    @Produces(MediaType.APPLICATION_XML)
    public Response postXml(String content) {
        //TODO
        return Response.created(context.getAbsolutePath()).build();
    }

    /**
     * Sub-resource locator method for {id}
     */
    @Path("{id}")
    public App_2RESTResource getApp_2RESTResource(@PathParam("id") String id) {
        return App_2RESTResource.getInstance(id);
    }
}
