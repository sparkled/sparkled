package net.chrisparton.xmas.rest;

import net.chrisparton.xmas.entity.AnimationEffectType;
import net.chrisparton.xmas.persistence.animation.AnimationEffectTypePersistenceService;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/animation-effect-type")
public class AnimationEffectTypeRestService extends RestService {

    private AnimationEffectTypePersistenceService persistenceService = new AnimationEffectTypePersistenceService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllAnimationEffectTypes() {
        List<AnimationEffectType> animationEffectTypes = persistenceService.getAllAnimationEffectTypes();
        return getJsonResponse(animationEffectTypes);
    }
}