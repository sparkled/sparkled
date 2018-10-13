package io.sparkled.rest.service.filltype;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/fillTypes")
public class FillTypeRestService {

    private final FillTypeRestServiceHandler handler;

    @Inject
    public FillTypeRestService(FillTypeRestServiceHandler handler) {
        this.handler = handler;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        return handler.get();
    }
}