package io.sparkled.rest.service.renderpreview;

import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.SequenceChannel;
import io.sparkled.model.entity.Song;
import io.sparkled.model.entity.StageProp;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.model.util.SequenceUtils;
import io.sparkled.model.validator.SequenceChannelValidator;
import io.sparkled.persistence.sequence.SequencePersistenceService;
import io.sparkled.persistence.song.SongPersistenceService;
import io.sparkled.persistence.stage.StagePersistenceService;
import io.sparkled.renderer.Renderer;
import io.sparkled.rest.service.RestServiceHandler;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

public class RenderPreviewRestServiceHandler extends RestServiceHandler {

    private final SequencePersistenceService sequencePersistenceService;
    private final StagePersistenceService stagePersistenceService;
    private final SongPersistenceService songPersistenceService;

    @Inject
    public RenderPreviewRestServiceHandler(SequencePersistenceService sequencePersistenceService,
                                           SongPersistenceService songPersistenceService,
                                           StagePersistenceService stagePersistenceService) {
        this.sequencePersistenceService = sequencePersistenceService;
        this.stagePersistenceService = stagePersistenceService;
        this.songPersistenceService = songPersistenceService;
    }

    Response getRenderedSequence(int startFrame, int frameCount, List<SequenceChannel> sequenceChannels) {
        if (sequenceChannels == null || sequenceChannels.isEmpty()) {
            return respond(Response.Status.BAD_REQUEST, "Nothing to render.");
        }

        Optional<Sequence> sequenceOptional = sequencePersistenceService.getSequenceById(sequenceChannels.get(0).getSequenceId());
        Optional<Song> songOptional = songPersistenceService.getSongBySequenceId(sequenceChannels.get(0).getSequenceId());

        if (!sequenceOptional.isPresent()) {
            return respond(Response.Status.NOT_FOUND, "Sequence not found.");
        } else if (!songOptional.isPresent()) {
            return respond(Response.Status.NOT_FOUND, "Song not found.");
        }

        Sequence sequence = sequenceOptional.get();
        Song song = songOptional.get();

        int sequenceFrameCount = SequenceUtils.getFrameCount(song, sequence);

        // Ensure the duration doesn't go past the end of the last available frame for the sequence.
        int endFrame = Math.min(sequenceFrameCount, startFrame + frameCount) - 1;
        RenderedStagePropDataMap renderResult = getRenderResult(sequence, startFrame, endFrame, sequenceChannels);

        return respondOk(renderResult);
    }

    private RenderedStagePropDataMap getRenderResult(Sequence sequence, int startFrame, int endFrame, List<SequenceChannel> sequenceChannels) {
        SequenceChannelValidator validator = new SequenceChannelValidator();
        sequenceChannels.forEach(validator::validate);

        List<StageProp> stageProps = stagePersistenceService.getStagePropsByStageId(sequence.getStageId());
        return new Renderer(sequence, sequenceChannels, stageProps, startFrame, endFrame).render();
    }
}