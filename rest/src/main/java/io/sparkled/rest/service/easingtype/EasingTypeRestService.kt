package io.sparkled.rest.service.easingtype;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/easingTypes")
public class EasingTypeRestService {

    private final EasingTypeRestServiceHandler handler;

    @Inject
    public EasingTypeRestService(EasingTypeRestServiceHandler handler) {
        this.handler = handler;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        return handler.get();
    }
}