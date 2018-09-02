package io.sparkled.rest;

import io.sparkled.model.animation.SequenceAnimationData;
import io.sparkled.model.animation.effect.EffectChannel;
import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.SequenceAnimation;
import io.sparkled.model.entity.SequenceStatus;
import io.sparkled.model.entity.SongAudio;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.model.validator.exception.EntityValidationException;
import io.sparkled.persistence.sequence.SequencePersistenceService;
import io.sparkled.renderer.Renderer;
import io.sparkled.rest.response.IdResponse;
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

@Path("/sequences")
public class SequenceRestService extends RestService {

    private static final String MP3_MIME_TYPE = "audio/mpeg";

    private final SequencePersistenceService sequencePersistenceService;

    @Inject
    public SequenceRestService(SequencePersistenceService sequencePersistenceService) {
        this.sequencePersistenceService = sequencePersistenceService;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSequence(@PathParam("id") int id) {
        Optional<Sequence> sequence = sequencePersistenceService.getSequenceById(id);

        if (sequence.isPresent()) {
            return getJsonResponse(sequence.get());
        }

        return getJsonResponse(Response.Status.NOT_FOUND, "Failed to find sequence.");
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
        }

        return getResponse(Response.Status.NOT_FOUND);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSequence(@FormDataParam("sequence") String sequenceJson,
                                   @FormDataParam("mp3") InputStream uploadedInputStream,
                                   @FormDataParam("mp3") FormDataContentDisposition fileDetail) throws IOException {
        try {
            Sequence sequence = gson.fromJson(sequenceJson, Sequence.class);
            int sequenceId = persistSequence(sequence);
            persistSongAudio(uploadedInputStream, sequenceId);
            persistSequenceAnimation(sequenceId);
            return getJsonResponse(new IdResponse(sequenceId));
        } catch (EntityValidationException e) {
            return getJsonResponse(Response.Status.BAD_REQUEST, e.getMessage());
        }
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
    public Response updateSequence(@PathParam("id") int id, Sequence sequence) {
        if (id != sequence.getId()) {
            return getJsonResponse(Response.Status.BAD_REQUEST, "Sequence ID does not match URL.");
        }

        try {
            Integer savedId = sequencePersistenceService.saveSequence(sequence);
            if (savedId == null) {
                return getJsonResponse(Response.Status.NOT_FOUND, "Sequence not found.");
            } else {
                IdResponse idResponse = new IdResponse(savedId);
                return getJsonResponse(idResponse);
            }
        } catch (EntityValidationException e) {
            return getJsonResponse(Response.Status.BAD_REQUEST, e.getMessage());
        }
    }

    @GET
    @Path("/{id}/animation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSequenceAnimation(@PathParam("id") int id) {
        Optional<SequenceAnimation> sequenceAnimationOptional = sequencePersistenceService.getSequenceAnimationBySequenceId(id);
        if (sequenceAnimationOptional.isPresent()) {
            return getJsonResponse(sequenceAnimationOptional.get());
        }
        return getResponse(Response.Status.NOT_FOUND);
    }

    @PUT
    @Path("/{id}/animation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSequenceAnimation(SequenceAnimation sequenceAnimation) {
        return saveOrSubmitAnimation(sequenceAnimation, SequenceStatus.DRAFT);
    }

    @PUT
    @Path("/{id}/render")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRenderedSequence(SequenceAnimation sequenceAnimation) {
        return saveOrSubmitAnimation(sequenceAnimation, SequenceStatus.PUBLISHED);
    }

    private Response saveOrSubmitAnimation(SequenceAnimation sequenceAnimation, SequenceStatus sequenceStatus) {
        try {
            Optional<Sequence> sequence = sequencePersistenceService.getSequenceById(sequenceAnimation.getSequenceId());

            if (sequence.isPresent()) {
                sequence.get().setStatus(sequenceStatus);
                sequencePersistenceService.saveSequence(sequence.get());
            } else {
                return getJsonResponse(Response.Status.NOT_FOUND, "Sequence not found.");
            }

            if (sequenceStatus == SequenceStatus.PUBLISHED) {
                persistRenderedSequence(sequence.get(), sequenceAnimation);
            }

            Integer savedId = sequencePersistenceService.saveSequenceAnimation(sequenceAnimation);
            if (savedId == null) {
                return getJsonResponse(Response.Status.NOT_FOUND, "Sequence animation not found.");
            } else {
                IdResponse idResponse = new IdResponse(savedId);
                return getJsonResponse(idResponse);
            }
        } catch (EntityValidationException e) {
            return getJsonResponse(Response.Status.BAD_REQUEST, e.getMessage());
        }
    }

    private int persistSequence(Sequence sequence) {
        sequence.setId(null);
        sequence.setStatus(SequenceStatus.NEW);
        return sequencePersistenceService.saveSequence(sequence);
    }

    private String createSequenceAnimationData() {
        // TODO: Remove hard-coded channels.
        SequenceAnimationData animationData = new SequenceAnimationData();

        for (int i = 1; i <= 4; i++) {
            addChannel(animationData, "Roof " + i, "R" + i, 50);
        }

        for (int i = 1; i <= 4; i++) {
            addChannel(animationData, "Pillar " + i, "P" + i, 140);
        }

        return gson.toJson(animationData);
    }

    private void addChannel(SequenceAnimationData animationData, String channelName, String channelCode, int ledCount) {
        int startLed = 0;
        int displayOrder = 0;

        List<EffectChannel> channels = animationData.getChannels();
        if (!channels.isEmpty()) {
            EffectChannel lastChannel = channels.get(channels.size() - 1);
            startLed = lastChannel.getEndLed() + 1;
            displayOrder = lastChannel.getDisplayOrder() + 1;
        }

        int endLed = startLed + ledCount - 1;
        EffectChannel channel = new EffectChannel()
                .setName(channelName)
                .setPropCode(channelCode)
                .setDisplayOrder(displayOrder)
                .setStartLed(startLed)
                .setEndLed(endLed);
        channels.add(channel);
    }

    private void persistSongAudio(InputStream uploadedInputStream, int sequenceId) throws IOException {
        byte[] bytes = IOUtils.toByteArray(uploadedInputStream);

        SongAudio songAudio = new SongAudio();
        songAudio.setSequenceId(sequenceId);
        songAudio.setAudioData(bytes);

        sequencePersistenceService.saveSongAudio(songAudio);
    }

    private void persistSequenceAnimation(int sequenceId) throws IOException {
        SequenceAnimation sequenceAnimation = new SequenceAnimation();
        sequenceAnimation.setSequenceId(sequenceId);
        sequenceAnimation.setAnimationData(createSequenceAnimationData());

        sequencePersistenceService.saveSequenceAnimation(sequenceAnimation);
    }

    private void persistRenderedSequence(Sequence sequence, SequenceAnimation sequenceAnimation) {
        RenderedStagePropDataMap renderedStagePropDataMap = new Renderer(sequence, sequenceAnimation).render();
        sequencePersistenceService.saveRenderedChannels(sequence, renderedStagePropDataMap);
    }
}