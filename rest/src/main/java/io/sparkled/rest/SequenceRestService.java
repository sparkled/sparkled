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
    @Path("/{id}/audio")
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
        byte[] songAudioData = IOUtils.toByteArray(uploadedInputStream);
        int sequenceId = saveNewSequence(sequence, songAudioData);
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
        sequenceViewModel.setId(id); // Prevent client-side ID tampering.

        Sequence sequence = sequenceViewModelConverter.fromViewModel(sequenceViewModel);
        List<SequenceChannel> sequenceChannels = sequenceViewModel.getChannels()
                .stream()
                .map(sequenceChannelViewModelConverter::fromViewModel)
                .collect(Collectors.toList());

        Integer savedId;
        if (sequence.getStatus() == SequenceStatus.PUBLISHED) {
            Integer stageId = sequence.getStageId();
            Optional<Stage> stageOptional = stagePersistenceService.getStageById(stageId);
            if (!stageOptional.isPresent()) {
                return getJsonResponse(Response.Status.NOT_FOUND, "Stage with ID of '" + stageId + "' not found.");
            }

            Stage stage = stageOptional.get();
            savedId = publishSequence(sequence, stage, sequenceChannels);
        } else {
            savedId = saveDraftSequence(sequence, sequenceChannels);
        }

        if (savedId == null) {
            return getJsonResponse(Response.Status.NOT_FOUND, "Sequence not found.");
        } else {
            IdResponse idResponse = new IdResponse(savedId);
            return getJsonResponse(idResponse);
        }
    }

    private Integer saveDraftSequence(Sequence sequence, List<SequenceChannel> sequenceChannels) {
        return sequencePersistenceService.saveSequence(sequence, sequenceChannels);
    }

    private Integer publishSequence(Sequence sequence, Stage stage, List<SequenceChannel> sequenceChannels) {
        List<StageProp> stageProps = stage.getStageProps();
        RenderedStagePropDataMap renderedStageProps = new Renderer(sequence, sequenceChannels, stageProps).render();
        return sequencePersistenceService.publishSequence(sequence, stage, sequenceChannels, renderedStageProps);
    }

    private int saveNewSequence(Sequence sequence, byte[] songAudioData) {
        sequence.setId(null);
        sequence.setStatus(SequenceStatus.NEW);
        return sequencePersistenceService.createSequence(sequence, songAudioData);
    }
}