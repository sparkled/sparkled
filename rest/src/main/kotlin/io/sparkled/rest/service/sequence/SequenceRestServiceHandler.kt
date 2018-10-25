package io.sparkled.rest.service.sequence;

import com.google.inject.persist.Transactional;
import io.sparkled.model.entity.*;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.model.util.SequenceUtils;
import io.sparkled.model.validator.exception.EntityNotFoundException;
import io.sparkled.persistence.sequence.SequencePersistenceService;
import io.sparkled.persistence.song.SongPersistenceService;
import io.sparkled.persistence.stage.StagePersistenceService;
import io.sparkled.renderer.Renderer;
import io.sparkled.rest.response.IdResponse;
import io.sparkled.rest.service.RestServiceHandler;
import io.sparkled.viewmodel.sequence.SequenceViewModel;
import io.sparkled.viewmodel.sequence.SequenceViewModelConverter;
import io.sparkled.viewmodel.sequence.channel.SequenceChannelViewModel;
import io.sparkled.viewmodel.sequence.channel.SequenceChannelViewModelConverter;
import io.sparkled.viewmodel.sequence.search.SequenceSearchViewModel;
import io.sparkled.viewmodel.sequence.search.SequenceSearchViewModelConverter;
import io.sparkled.viewmodel.stage.StageViewModel;
import io.sparkled.viewmodel.stage.StageViewModelConverter;
import io.sparkled.viewmodel.stage.prop.StagePropViewModel;
import io.sparkled.viewmodel.stage.prop.StagePropViewModelConverter;

import javax.inject.Inject;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

class SequenceRestServiceHandler extends RestServiceHandler {

    private static final String MP3_MIME_TYPE = "audio/mpeg";

    private final SequencePersistenceService sequencePersistenceService;
    private final SongPersistenceService songPersistenceService;
    private final StagePersistenceService stagePersistenceService;
    private final SequenceViewModelConverter sequenceViewModelConverter;
    private final SequenceSearchViewModelConverter sequenceSearchViewModelConverter;
    private final SequenceChannelViewModelConverter sequenceChannelViewModelConverter;
    private final StageViewModelConverter stageViewModelConverter;
    private final StagePropViewModelConverter stagePropViewModelConverter;

    @Inject
    SequenceRestServiceHandler(SequencePersistenceService sequencePersistenceService,
                               SongPersistenceService songPersistenceService,
                               StagePersistenceService stagePersistenceService,
                               SequenceViewModelConverter sequenceViewModelConverter,
                               SequenceSearchViewModelConverter sequenceSearchViewModelConverter,
                               SequenceChannelViewModelConverter sequenceChannelViewModelConverter,
                               StageViewModelConverter stageViewModelConverter,
                               StagePropViewModelConverter stagePropViewModelConverter) {
        this.sequencePersistenceService = sequencePersistenceService;
        this.songPersistenceService = songPersistenceService;
        this.stagePersistenceService = stagePersistenceService;
        this.sequenceViewModelConverter = sequenceViewModelConverter;
        this.sequenceSearchViewModelConverter = sequenceSearchViewModelConverter;
        this.sequenceChannelViewModelConverter = sequenceChannelViewModelConverter;
        this.stageViewModelConverter = stageViewModelConverter;
        this.stagePropViewModelConverter = stagePropViewModelConverter;
    }

    @Transactional
    Response createSequence(SequenceViewModel sequenceViewModel) {
        sequenceViewModel.setId(null);
        sequenceViewModel.setStatus(SequenceStatus.NEW);

        Sequence sequence = sequenceViewModelConverter.toModel(sequenceViewModel);
        sequence = sequencePersistenceService.createSequence(sequence);
        return respondOk(new IdResponse(sequence.getId()));
    }

    Response getAllSequences() {
        List<Sequence> sequences = sequencePersistenceService.getAllSequences();
        List<SequenceSearchViewModel> viewModels = sequenceSearchViewModelConverter.toViewModels(sequences);
        return respondOk(viewModels);
    }

    Response getSequence(int sequenceId) {
        Optional<Sequence> sequenceOptional = sequencePersistenceService.getSequenceById(sequenceId);

        if (sequenceOptional.isPresent()) {
            Sequence sequence = sequenceOptional.get();
            SequenceViewModel viewModel = sequenceViewModelConverter.toViewModel(sequence);

            List<SequenceChannelViewModel> channels = sequencePersistenceService
                    .getSequenceChannelsBySequenceId(sequenceId)
                    .stream()
                    .map(sequenceChannelViewModelConverter::toViewModel)
                    .collect(toList());
            viewModel.setChannels(channels);

            return respondOk(viewModel);
        }

        return respond(Response.Status.NOT_FOUND, "Sequence not found.");
    }

    Response getSequenceStage(int sequenceId) {
        Optional<Stage> stageOptional = sequencePersistenceService.getStageBySequenceId(sequenceId);

        if (stageOptional.isPresent()) {
            Stage stage = stageOptional.get();

            StageViewModel viewModel = stageViewModelConverter.toViewModel(stage);
            List<StagePropViewModel> stageProps = stagePersistenceService
                    .getStagePropsByStageId(stage.getId())
                    .stream()
                    .map(stagePropViewModelConverter::toViewModel)
                    .collect(toList());
            viewModel.setStageProps(stageProps);
            return respondOk(viewModel);
        } else {
            return respond(Response.Status.NOT_FOUND, "Stage not found.");
        }
    }

    Response getSequenceSongAudio(int sequenceId) {
        Optional<SongAudio> songAudioOptional = sequencePersistenceService.getSongAudioBySequenceId(sequenceId);
        if (songAudioOptional.isPresent()) {
            return respondMedia(songAudioOptional.get().getAudioData(), MP3_MIME_TYPE);
        } else {
            return respond(Response.Status.NOT_FOUND, "Song audio not found.");
        }
    }

    @Transactional
    Response updateSequence(int id, SequenceViewModel sequenceViewModel) {
        sequenceViewModel.setId(id); // Prevent client-side ID tampering.

        Sequence sequence = sequenceViewModelConverter.toModel(sequenceViewModel);
        List<SequenceChannel> sequenceChannels = sequenceViewModel.getChannels()
                .stream()
                .map(sequenceChannelViewModelConverter::toModel)
                .collect(toList());

        if (sequence.getStatus() == SequenceStatus.PUBLISHED) {
            publishSequence(sequence, sequenceChannels);
        } else {
            saveDraftSequence(sequence, sequenceChannels);
        }

        return respondOk();
    }

    @Transactional
    Response deleteSequence(int id) {
        sequencePersistenceService.deleteSequence(id);
        return respondOk();
    }

    private void saveDraftSequence(Sequence sequence, List<SequenceChannel> sequenceChannels) {
        sequencePersistenceService.saveSequence(sequence, sequenceChannels);
    }

    private void publishSequence(Sequence sequence, List<SequenceChannel> sequenceChannels) {
        Song song = songPersistenceService.getSongBySequenceId(sequence.getId())
                .orElseThrow(() -> new EntityNotFoundException("Song not found."));

        List<StageProp> stageProps = stagePersistenceService.getStagePropsByStageId(sequence.getStageId());
        int endFrame = SequenceUtils.getFrameCount(song, sequence) - 1;

        RenderedStagePropDataMap renderedStageProps = new Renderer(sequence, sequenceChannels, stageProps, 0, endFrame).render();
        sequencePersistenceService.publishSequence(sequence, sequenceChannels, renderedStageProps);
    }
}