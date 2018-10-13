package io.sparkled.rest.service.renderpreview;

import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.SequenceChannel;
import io.sparkled.model.entity.StageProp;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.model.validator.SequenceChannelValidator;
import io.sparkled.persistence.sequence.SequencePersistenceService;
import io.sparkled.renderer.Renderer;
import io.sparkled.rest.service.RestServiceHandler;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RenderPreviewRestServiceHandler extends RestServiceHandler {

    private final SequencePersistenceService sequencePersistenceService;

    @Inject
    public RenderPreviewRestServiceHandler(SequencePersistenceService sequencePersistenceService) {
        this.sequencePersistenceService = sequencePersistenceService;
    }

    Response getRenderedSequence(int startFrame, int durationFrames, List<SequenceChannel> sequenceChannels) {
        if (sequenceChannels == null || sequenceChannels.isEmpty()) {
            return respond(Response.Status.BAD_REQUEST, "Nothing to render.");
        }

        Optional<Sequence> sequenceOptional = sequencePersistenceService.getSequenceById(sequenceChannels.get(0).getSequenceId());

        if (sequenceOptional.isPresent()) {
            SequenceChannelValidator validator = new SequenceChannelValidator();
            sequenceChannels.forEach(validator::validate);

            Sequence sequence = sequenceOptional.get();

            List<StageProp> stageProps = Collections.singletonList(new StageProp().setLedCount(10)); // TODO: Get from DB.
            RenderedStagePropDataMap renderResult = new Renderer(sequence, sequenceChannels, stageProps, startFrame, durationFrames).render();
            return respondOk(renderResult);
        } else {
            return respond(Response.Status.NOT_FOUND, "Sequence not found.");
        }
    }
}