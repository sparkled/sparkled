package io.sparkled.rest;

import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.SequenceAnimation;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.model.validator.SequenceAnimationValidator;
import io.sparkled.renderer.Renderer;
import io.sparkled.persistence.sequence.SequencePersistenceService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/renderPreview")
public class RenderPreviewRestService extends RestService {

    private final SequencePersistenceService sequencePersistenceService;

    @Inject
    public RenderPreviewRestService(SequencePersistenceService sequencePersistenceService) {
        this.sequencePersistenceService = sequencePersistenceService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRenderedSequence(@QueryParam("startFrame") int startFrame,
                                        @QueryParam("durationFrames") int durationFrames,
                                        SequenceAnimation sequenceAnimation) {

        Optional<Sequence> sequenceOptional = sequencePersistenceService.getSequenceById(sequenceAnimation.getSequenceId());

        if (sequenceOptional.isPresent()) {
            SequenceAnimationValidator sequenceAnimationValidator = new SequenceAnimationValidator();
            sequenceAnimationValidator.validate(sequenceAnimation);

            Sequence sequence = sequenceOptional.get();
            RenderedStagePropDataMap renderResult = new Renderer(sequence, sequenceAnimation, startFrame, durationFrames).render();
            return getJsonResponse(renderResult);
        } else {
            return getJsonResponse(Response.Status.NOT_FOUND, "Sequence not found.");
        }
    }
}