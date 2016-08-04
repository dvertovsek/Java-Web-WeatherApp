/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nwtis.dvertovs.rest.serveri;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import javax.servlet.ServletContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.PathParam;
import javax.ws.rs.POST;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import nwtis.dvertovs.db.DBController;
import nwtis.dvertovs.ws.serveri.DvertovsAplikacija1WS;
import org.foi.nwtis.dvertovs.konfiguracije.Konfiguracija;

/**
 * REST Web Service
 *
 * @author dare
 */
@Path("/meteoREST")
public class MeteoRESTsResource {

    @Context
    private ServletContext servletContext;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of MeteoRESTsResource
     */
    public MeteoRESTsResource() {
    }

    /**
     * Retrieves representation of an instance of
     * nwtis.dvertovs.rest.serveri.MeteoRESTsResource
     *
     * @param user
     * @param pass
     * @return an instance of java.lang.String
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     * @throws java.sql.SQLException
     */
    @GET
    @Path("getAddresses/{user}/{pass}/")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@PathParam("user") String user, @PathParam("pass") String pass) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

        JsonArrayBuilder jab = Json.createArrayBuilder();

        DvertovsAplikacija1WS daws = new DvertovsAplikacija1WS();
        if (daws.userAuthenticated("USER " + user + "; PASSWD " + pass + ";", this.getQuota(), "REST", "getAddresses")){

            ResultSet rsAddr = DBController.getDbCon().getAddresses("", "");

            while (rsAddr.next()) {
                jab.add(rsAddr.getString("adresa"));
            }
        }
        JsonObjectBuilder jbf = Json.createObjectBuilder();
        jbf.add("adrese", jab);
        return jbf.build().toString();
    }

    @GET
    @Path("currentForecast/{user}/{pass}/{id}/")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@PathParam("user") String user, @PathParam("pass") String pass, @PathParam("id") int id) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException {

        JsonArrayBuilder jab = Json.createArrayBuilder();

        DvertovsAplikacija1WS daws = new DvertovsAplikacija1WS();
        if (daws.userAuthenticated("USER " + user + "; PASSWD " + pass + ";", this.getQuota(), "REST", "currentForecast")){

            ResultSet rs = DBController.getDbCon().getLastForecast(id);

            jab = getJSONArray(rs);
        }
        JsonObjectBuilder jbf = Json.createObjectBuilder();
        jbf.add("forecast", jab);
        return jbf.build().toString();
    }

    @GET
    @Path("specificForecast/{user}/{pass}/{id}/{time}/")
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@PathParam("user") String user, @PathParam("pass") String pass, @PathParam("id") int id, @PathParam("time") String time) throws ClassNotFoundException, InstantiationException, IllegalAccessException, SQLException, ParseException {

        JsonArrayBuilder jab = Json.createArrayBuilder();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        DvertovsAplikacija1WS daws = new DvertovsAplikacija1WS();
        if (daws.userAuthenticated("USER " + user + "; PASSWD " + pass + ";", this.getQuota(), "REST", "specificForecast")){
            
            ResultSet rs = DBController.getDbCon().getSpecificForecast(id, df.parse(time));

            jab = getJSONArray(rs);
        }
        JsonObjectBuilder jbf = Json.createObjectBuilder();
        jbf.add("forecast", jab);
        return jbf.build().toString();
    }

    private JsonArrayBuilder getJSONArray(ResultSet rs) throws SQLException {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        while (rs.next()) {
            JsonObjectBuilder jbf = Json.createObjectBuilder();
            jbf.add("vrijemeopis", rs.getString("vrijemeopis"));
            jbf.add("temp", rs.getString("temp"));
            jbf.add("vlaga", rs.getString("vlaga"));
            jbf.add("tlak", rs.getString("tlak"));
            jbf.add("vrijemeprognoze", rs.getString("vrijemeprognoze"));
            jbf.add("preuzeto", rs.getString("preuzeto"));
            jab.add(jbf);
        }
        return jab;
    }

    /**
     * POST method for creating an instance of MeteoRESTResource
     *
     * @param content representation for the new resource
     * @return an HTTP response with content of the created resource
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postJson(String content
    ) {
        //TODO
        return Response.created(context.getAbsolutePath()).build();
    }

    /**
     * Sub-resource locator method for {id}
     */
    @Path("{id}")
    public MeteoRESTResource getMeteoRESTResource(@PathParam("id") String id
    ) {
        return MeteoRESTResource.getInstance(id);
    }

    protected int getQuota() {
        Konfiguracija konfig = (Konfiguracija) this.servletContext.getAttribute("App_Konfig");
        return Integer.parseInt(konfig.dajPostavku("kvotaZahtjeva"));
    }
}
