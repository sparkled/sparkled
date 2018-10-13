package io.sparkled.rest.service.sequence;

import com.google.inject.persist.Transactional;
import io.sparkled.model.entity.*;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.persistence.sequence.SequencePersistenceService;
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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class SequenceRestServiceHandler extends RestServiceHandler {

    private final SequencePersistenceService sequencePersistenceService;
    private final StagePersistenceService stagePersistenceService;
    private final SequenceViewModelConverter sequenceViewModelConverter;
    private final SequenceSearchViewModelConverter sequenceSearchViewModelConverter;
    private final SequenceChannelViewModelConverter sequenceChannelViewModelConverter;
    private final StageViewModelConverter stageViewModelConverter;
    private final StagePropViewModelConverter stagePropViewModelConverter;

    @Inject
    SequenceRestServiceHandler(SequencePersistenceService sequencePersistenceService,
                               StagePersistenceService stagePersistenceService,
                               SequenceViewModelConverter sequenceViewModelConverter,
                               SequenceSearchViewModelConverter sequenceSearchViewModelConverter,
                               SequenceChannelViewModelConverter sequenceChannelViewModelConverter,
                               StageViewModelConverter stageViewModelConverter,
                               StagePropViewModelConverter stagePropViewModelConverter) {
        this.sequencePersistenceService = sequencePersistenceService;
        this.stagePersistenceService = stagePersistenceService;
        this.sequenceViewModelConverter = sequenceViewModelConverter;
        this.sequenceSearchViewModelConverter = sequenceSearchViewModelConverter;
        this.sequenceChannelViewModelConverter = sequenceChannelViewModelConverter;
        this.stageViewModelConverter = stageViewModelConverter;
        this.stagePropViewModelConverter = stagePropViewModelConverter;
    }

    @Transactional
    Response createSequence(String sequenceJson, InputStream uploadedInputStream) throws IOException {
        SequenceViewModel sequenceViewModel = gson.fromJson(sequenceJson, SequenceViewModel.class);
        Sequence sequence = sequenceViewModelConverter.toModel(sequenceViewModel);

        byte[] songAudioData = loadSongData(uploadedInputStream);
        int sequenceId = saveNewSequence(sequence, songAudioData);
        return getJsonResponse(new IdResponse(sequenceId));
    }

    // TODO: Use IOUtils.toByteArray() after moving to Java 9.
    private byte[] loadSongData(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        int offset;
        byte[] buffer = new byte[4096];
        while (-1 != (offset = inputStream.read(buffer))) {
            outputStream.write(buffer, 0, offset);
        }

        return outputStream.toByteArray();
    }

    Response getAllSequences() {
        List<Sequence> sequences = sequencePersistenceService.getAllSequences();
        List<SequenceSearchViewModel> results = sequences.stream()
                .map(sequenceSearchViewModelConverter::toViewModel)
                .collect(Collectors.toList());

        return getJsonResponse(results);
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
                    .collect(Collectors.toList());
            viewModel.setChannels(channels);

            return getJsonResponse(viewModel);
        }

        return getResponse(Response.Status.NOT_FOUND);
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
                    .collect(Collectors.toList());
            viewModel.setStageProps(stageProps);
            return getJsonResponse(viewModel);
        } else {
            return getResponse(Response.Status.NOT_FOUND);
        }
    }

    Response getSequenceSongAudio(int id) {
        Optional<SongAudio> songAudio = sequencePersistenceService.getSongAudioBySequenceId(id);

        if (songAudio.isPresent()) {
            return getBinaryResponse(songAudio.get().getAudioData(), SequenceRestService.MP3_MIME_TYPE);
        } else {
            return getResponse(Response.Status.NOT_FOUND);
        }
    }

    @Transactional
    Response updateSequence(int id, SequenceViewModel sequenceViewModel) {
        sequenceViewModel.setId(id); // Prevent client-side ID tampering.

        Sequence sequence = sequenceViewModelConverter.toModel(sequenceViewModel);
        List<SequenceChannel> sequenceChannels = sequenceViewModel.getChannels()
                .stream()
                .map(sequenceChannelViewModelConverter::toModel)
                .collect(Collectors.toList());

        if (sequence.getStatus() == SequenceStatus.PUBLISHED) {
            publishSequence(sequence, sequenceChannels);
        } else {
            saveDraftSequence(sequence, sequenceChannels);
        }
        return getResponse(Response.Status.OK);
    }

    @Transactional
    Response deleteSequence(int id) {
        sequencePersistenceService.deleteSequence(id);
        return getResponse(Response.Status.OK);
    }

    private void saveDraftSequence(Sequence sequence, List<SequenceChannel> sequenceChannels) {
        sequencePersistenceService.saveSequence(sequence, sequenceChannels);
    }

    private void publishSequence(Sequence sequence, List<SequenceChannel> sequenceChannels) {
        List<StageProp> stageProps = stagePersistenceService.getStagePropsByStageId(sequence.getStageId());
        RenderedStagePropDataMap renderedStageProps = new Renderer(sequence, sequenceChannels, stageProps).render();
        sequencePersistenceService.publishSequence(sequence, sequenceChannels, renderedStageProps);
    }

    private int saveNewSequence(Sequence sequence, byte[] songAudioData) {
        sequence.setId(null);
        sequence.setStatus(SequenceStatus.NEW);
        return sequencePersistenceService.createSequence(sequence, songAudioData);
    }
}