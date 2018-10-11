package io.sparkled.rest;

import io.sparkled.model.entity.*;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.persistence.sequence.SequencePersistenceService;
import io.sparkled.persistence.stage.StagePersistenceService;
import io.sparkled.renderer.Renderer;
import io.sparkled.rest.response.IdResponse;
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
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/sequences")
public class SequenceRestService extends RestService {

    private static final String MP3_MIME_TYPE = "audio/mpeg";

    private final SequencePersistenceService sequencePersistenceService;
    private final StagePersistenceService stagePersistenceService;
    private final SequenceViewModelConverter sequenceViewModelConverter;
    private final SequenceSearchViewModelConverter sequenceSearchViewModelConverter;
    private final SequenceChannelViewModelConverter sequenceChannelViewModelConverter;
    private final StageViewModelConverter stageViewModelConverter;
    private final StagePropViewModelConverter stagePropViewModelConverter;

    @Inject
    public SequenceRestService(SequencePersistenceService sequencePersistenceService,
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

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSequence(@FormDataParam("sequence") String sequenceJson,
                                   @FormDataParam("mp3") InputStream uploadedInputStream,
                                   @FormDataParam("mp3") FormDataContentDisposition fileDetail) throws IOException {
        SequenceViewModel sequenceViewModel = gson.fromJson(sequenceJson, SequenceViewModel.class);
        Sequence sequence = sequenceViewModelConverter.toModel(sequenceViewModel);

        byte[] songAudioData = IOUtils.toByteArray(uploadedInputStream);
        int sequenceId = saveNewSequence(sequence, songAudioData);
        return getJsonResponse(new IdResponse(sequenceId));
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSequences() {
        List<Sequence> sequences = sequencePersistenceService.getAllSequences();
        List<SequenceSearchViewModel> results = sequences.stream()
                .map(sequenceSearchViewModelConverter::toViewModel)
                .collect(Collectors.toList());

        return getJsonResponse(results);
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSequence(@PathParam("id") int sequenceId) {
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

        return getJsonResponse(Response.Status.NOT_FOUND);
    }

    @GET
    @Path("/{id}/stage")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSequenceStage(@PathParam("id") int sequenceId) {
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
            return getJsonResponse(Response.Status.NOT_FOUND);
        }
    }

    @GET
    @Path("/{id}/audio")
    @Produces(MP3_MIME_TYPE)
    public Response getSequenceSongAudio(@PathParam("id") int id) {
        Optional<SongAudio> songAudio = sequencePersistenceService.getSongAudioBySequenceId(id);

        if (songAudio.isPresent()) {
            return getBinaryResponse(songAudio.get().getAudioData(), MP3_MIME_TYPE);
        } else {
            return getResponse(Response.Status.NOT_FOUND);
        }
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSequence(@PathParam("id") int id, SequenceViewModel sequenceViewModel) {
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

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSequence(@PathParam("id") int id) {
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