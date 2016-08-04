/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.foi.nwtis.dvertovs.ws.klijenti;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;

/**
 * Jersey REST client generated for REST resource:MeteoRESTResourceContainer
 * [/meteoREST]<br>
 * USAGE:
 * <pre>
 * RESTClient client = new RESTClient();
 * Object response = client.XXX(...);
 * // do whatever with response
 * client.close();
 * </pre>
 *
 * @author dare
 */
public class RESTClient {

    private WebTarget webTarget;
    private final Client client;

    public RESTClient() {
        client = javax.ws.rs.client.ClientBuilder.newClient();
    }

    public void setTarget(String BASE_URI, String path) {
        webTarget = client.target(BASE_URI).path(path);
        webTarget = webTarget.queryParam("units", "metric");
        webTarget = webTarget.queryParam("lang", "hr");
    }

    public void setParam(String name, String value) {
        webTarget = webTarget.queryParam(name, value);
    }

    public Response postJson(Object requestEntity) throws ClientErrorException {
        return webTarget.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).post(javax.ws.rs.client.Entity.entity(requestEntity, javax.ws.rs.core.MediaType.APPLICATION_JSON), Response.class);
    }

    public String getJson() throws ClientErrorException {
        WebTarget resource = webTarget;
        return resource.request(javax.ws.rs.core.MediaType.APPLICATION_JSON).get(String.class);
    }

    public void close() {
        client.close();
    }

}
