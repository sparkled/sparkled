package io.sparkled.rest;

import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.SequenceChannel;
import io.sparkled.model.entity.StageProp;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.model.validator.SequenceChannelValidator;
import io.sparkled.persistence.sequence.SequencePersistenceService;
import io.sparkled.renderer.Renderer;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
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
                                        List<SequenceChannel> sequenceChannels) {

        if (sequenceChannels == null || sequenceChannels.isEmpty()) {
            return getJsonResponse(Response.Status.BAD_REQUEST, "Nothing to render.");
        }

        Optional<Sequence> sequenceOptional = sequencePersistenceService.getSequenceById(sequenceChannels.get(0).getSequenceId());

        if (sequenceOptional.isPresent()) {
            SequenceChannelValidator validator = new SequenceChannelValidator();
            sequenceChannels.forEach(validator::validate);

            Sequence sequence = sequenceOptional.get();

            List<StageProp> stageProps = Collections.singletonList(new StageProp().setLeds(10)); // TODO: Get from DB.
            RenderedStagePropDataMap renderResult = new Renderer(sequence, sequenceChannels, stageProps, startFrame, durationFrames).render();
            return getJsonResponse(renderResult);
        } else {
            return getJsonResponse(Response.Status.NOT_FOUND);
        }
    }
}