package net.chrisparton.xmas.rest;

import net.chrisparton.xmas.entity.AnimationEffectType;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/animation-effect-type")
public class AnimationEffectTypeRestService extends RestService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAnimationEffectTypes() {
        return getJsonResponse(AnimationEffectType.TYPES);
    }
}