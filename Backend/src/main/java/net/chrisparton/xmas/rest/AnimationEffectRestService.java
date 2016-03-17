package net.chrisparton.xmas.rest;

import net.chrisparton.xmas.entity.AnimationEffect;
import net.chrisparton.xmas.persistence.animation.AnimationEffectPersistenceService;
import net.chrisparton.xmas.rest.RestService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/animation-effect")
public class AnimationEffectRestService extends RestService {

    private AnimationEffectPersistenceService persistenceService = new AnimationEffectPersistenceService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSongs() {
        List<AnimationEffect> animationEffects = persistenceService.getAllAnimationEffects();
        return getJsonResponse(animationEffects);
    }
}