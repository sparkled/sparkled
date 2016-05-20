package net.chrisparton.sparkled.rest;

import net.chrisparton.sparkled.entity.AnimationEasingTypeCode;
import net.chrisparton.sparkled.entity.AnimationEffectTypeCode;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/animation-easing-type")
public class AnimationEasingTypeRestService extends RestService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAnimationEasingTypes() {
        return getJsonResponse(AnimationEasingTypeCode.EASING_TYPES);
    }
}