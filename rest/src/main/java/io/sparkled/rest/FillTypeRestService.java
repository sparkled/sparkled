package io.sparkled.rest;

import io.sparkled.model.animation.fill.reference.FillTypes;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/fill-type")
public class FillTypeRestService extends RestService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        return getJsonResponse(FillTypes.get());
    }
}