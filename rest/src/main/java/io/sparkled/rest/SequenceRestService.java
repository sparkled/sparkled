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
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/sequences")
public class SequenceRestService extends RestService {

    private static final String MP3_MIME_TYPE = "audio/mpeg";

    private final SequencePersistenceService sequencePersistenceService;
    private final StagePersistenceService stagePersistenceService;
    private final SequenceViewModelConverter sequenceViewModelConverter;
    private final SequenceChannelViewModelConverter sequenceChannelViewModelConverter;

    @Inject
    public SequenceRestService(SequencePersistenceService sequencePersistenceService,
                               StagePersistenceService stagePersistenceService,
                               SequenceViewModelConverter sequenceViewModelConverter,
                               SequenceChannelViewModelConverter sequenceChannelViewModelConverter) {
        this.sequencePersistenceService = sequencePersistenceService;
        this.stagePersistenceService = stagePersistenceService;
        this.sequenceViewModelConverter = sequenceViewModelConverter;
        this.sequenceChannelViewModelConverter = sequenceChannelViewModelConverter;
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

        return getJsonResponse(Response.Status.NOT_FOUND, "Sequence with ID of '" + sequenceId + "' not found.");
    }

    @GET
    @Path("/{id}/stages")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSequenceStage(@PathParam("id") int sequenceId) {
        Optional<Stage> stage = sequencePersistenceService.getStageBySequenceId(sequenceId);

        if (stage.isPresent()) {
            return getJsonResponse(stage.get());
        } else {
            return getJsonResponse(Response.Status.NOT_FOUND, "Failed to find stage for sequence.");
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSequences() {
        List<Sequence> sequences = sequencePersistenceService.getAllSequences();
        return getJsonResponse(sequences);
    }

    @GET
    @Path("/data/{id}")
    @Produces(MP3_MIME_TYPE)
    public Response getSongAudio(@PathParam("id") int id) {
        Optional<SongAudio> songAudio = sequencePersistenceService.getSongAudioBySequenceId(id);

        if (songAudio.isPresent()) {
            return getBinaryResponse(songAudio.get().getAudioData(), MP3_MIME_TYPE);
        } else {
            return getResponse(Response.Status.NOT_FOUND);
        }
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSequence(@FormDataParam("sequence") String sequenceJson,
                                   @FormDataParam("mp3") InputStream uploadedInputStream,
                                   @FormDataParam("mp3") FormDataContentDisposition fileDetail) throws IOException {
        Sequence sequence = gson.fromJson(sequenceJson, Sequence.class);
        int sequenceId = persistSequence(sequence);
        persistSongAudio(uploadedInputStream, sequenceId);
        return getJsonResponse(new IdResponse(sequenceId));
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSequence(@PathParam("id") int id) {
        int sequenceId = sequencePersistenceService.deleteSequence(id);
        return getJsonResponse(new IdResponse(sequenceId));
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSequence(@PathParam("id") int id, SequenceViewModel sequenceViewModel) {
        if (id != sequenceViewModel.getId()) {
            return getJsonResponse(Response.Status.BAD_REQUEST, "Sequence ID does not match URL.");
        }

        Sequence sequence = sequenceViewModelConverter.fromViewModel(sequenceViewModel);
        List<SequenceChannel> sequenceChannels = sequenceViewModel.getChannels()
                .stream()
                .map(sequenceChannelViewModelConverter::fromViewModel)
                .collect(Collectors.toList());

        Integer savedId = sequencePersistenceService.saveSequence(sequence, sequenceChannels);
        if (savedId == null) {
            return getJsonResponse(Response.Status.NOT_FOUND, "Sequence not found.");
        } else {
            IdResponse idResponse = new IdResponse(savedId);
            return getJsonResponse(idResponse);
        }
    }

    @PUT
    @Path("/{id}/animation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSequenceAnimation(List<SequenceChannel> sequenceChannels) {
        return saveOrSubmitAnimation(sequenceChannels, SequenceStatus.DRAFT);
    }

    @PUT
    @Path("/{id}/render")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRenderedSequence(List<SequenceChannel> sequenceChannels) {
        return saveOrSubmitAnimation(sequenceChannels, SequenceStatus.PUBLISHED);
    }

    private Response saveOrSubmitAnimation(List<SequenceChannel> sequenceChannels, SequenceStatus sequenceStatus) {
        Optional<Sequence> sequenceOptional = sequencePersistenceService.getSequenceById(sequenceChannels.get(0).getSequenceId());

        if (!sequenceOptional.isPresent()) {
            return getJsonResponse(Response.Status.NOT_FOUND, "Sequence not found.");
        } else {
            Sequence sequence = sequenceOptional.get();
            sequence.setStatus(sequenceStatus);
            sequencePersistenceService.saveSequence(sequence, sequenceChannels);

            if (sequenceStatus == SequenceStatus.PUBLISHED) {
                Integer stageId = sequence.getStageId();
                Optional<Stage> stageOptional = stagePersistenceService.getStageById(stageId);
                if (!stageOptional.isPresent()) {
                    return getJsonResponse(Response.Status.NOT_FOUND, "Stage with ID of '" + stageId + "' not found.");
                } else {
                    Stage stage = stageOptional.get();
                    persistRenderedSequence(sequence, stage, sequenceChannels);
                }
            }
            return getJsonResponse(Response.ok());
        }
    }

    private void persistRenderedSequence(Sequence sequence, Stage stage, List<SequenceChannel> sequenceChannels) {
        List<StageProp> stageProps = stage.getStageProps();
        RenderedStagePropDataMap renderedStagePropDataMap = new Renderer(sequence, sequenceChannels, stageProps).render();
        sequencePersistenceService.saveRenderedChannels(sequence, renderedStagePropDataMap);
    }

    private int persistSequence(Sequence sequence) {
        sequence.setId(null);
        sequence.setStatus(SequenceStatus.NEW);
        return sequencePersistenceService.saveSequence(sequence, Collections.emptyList());
    }

    private void persistSongAudio(InputStream uploadedInputStream, int sequenceId) throws IOException {
        byte[] bytes = IOUtils.toByteArray(uploadedInputStream);

        SongAudio songAudio = new SongAudio();
        songAudio.setSequenceId(sequenceId);
        songAudio.setAudioData(bytes);

        sequencePersistenceService.saveSongAudio(songAudio);
    }
}