package io.sparkled.rest.service.effecttype;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/effectTypes")
public class EffectTypeRestService {

    private final EffectTypeRestServiceHandler handler;

    @Inject
    public EffectTypeRestService(EffectTypeRestServiceHandler handler) {
        this.handler = handler;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        return handler.get();
    }
}