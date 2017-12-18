package io.sparkled.rest;

import io.sparkled.model.animation.SongAnimationData;
import io.sparkled.model.animation.effect.EffectChannel;
import io.sparkled.model.entity.*;
import io.sparkled.model.render.RenderedChannelMap;
import io.sparkled.model.validator.exception.EntityValidationException;
import io.sparkled.renderer.Renderer;
import io.sparkled.rest.response.IdResponse;
import io.sparkled.persistence.song.SongPersistenceService;
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

@Path("/songs")
public class SongRestService extends RestService {

    private static final String MP3_MIME_TYPE = "audio/mpeg";

    private final SongPersistenceService songPersistenceService;

    @Inject
    public SongRestService(SongPersistenceService songPersistenceService) {
        this.songPersistenceService = songPersistenceService;
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSong(@PathParam("id") int id) {
        Optional<Song> song = songPersistenceService.getSongById(id);

        if (song.isPresent()) {
            return getJsonResponse(song.get());
        }

        return getJsonResponse(Response.Status.NOT_FOUND, "Failed to find song.");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSongs() {
        List<Song> songs = songPersistenceService.getAllSongs();
        return getJsonResponse(songs);
    }

    @GET
    @Path("/data/{id}")
    @Produces(MP3_MIME_TYPE)
    public Response getSongData(@PathParam("id") int id) {
        Optional<SongAudio> songData = songPersistenceService.getSongDataById(id);

        if (songData.isPresent()) {
            return getBinaryResponse(songData.get().getAudioData(), MP3_MIME_TYPE);
        }

        return getResponse(Response.Status.NOT_FOUND);
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSong(@FormDataParam("song") String songJson,
                               @FormDataParam("mp3") InputStream uploadedInputStream,
                               @FormDataParam("mp3") FormDataContentDisposition fileDetail) {

        try {
            Song song = gson.fromJson(songJson, Song.class);
            int songId = persistSong(song);
            persistSongAudio(uploadedInputStream, songId);
            persistSongAnimation(songId);
            return getJsonResponse(new IdResponse(songId));
        } catch (EntityValidationException e) {
            return getJsonResponse(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (IOException e) {
            return getResponse(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteSong(@PathParam("id") int id) {
        int songId = songPersistenceService.deleteSong(id);
        return getJsonResponse(new IdResponse(songId));
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSong(Song song) {
        try {
            Integer savedId = songPersistenceService.saveSong(song);
            if (savedId == null) {
                return getJsonResponse(Response.Status.NOT_FOUND, "Song not found.");
            } else {
                IdResponse idResponse = new IdResponse(savedId);
                return getJsonResponse(idResponse);
            }
        } catch (EntityValidationException e) {
            return getJsonResponse(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return getJsonResponse(Response.Status.INTERNAL_SERVER_ERROR, "An internal error occurred.");
        }
    }

    @GET
    @Path("/{id}/animation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSongAnimation(@PathParam("id") int id) {
        Optional<SongAnimation> songAnimationOptional = songPersistenceService.getSongAnimationById(id);
        if (songAnimationOptional.isPresent()) {
            return getJsonResponse(songAnimationOptional.get());
        }
        return getResponse(Response.Status.NOT_FOUND);
    }

    @PUT
    @Path("/{id}/animation")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSongAnimation(SongAnimation songAnimation) {
        return saveOrSubmitAnimation(songAnimation, SongStatus.DRAFT);
    }

    @PUT
    @Path("/{id}/render")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRenderedSong(SongAnimation songAnimation) {
        return saveOrSubmitAnimation(songAnimation, SongStatus.PUBLISHED);
    }

    private Response saveOrSubmitAnimation(SongAnimation songAnimation, SongStatus songStatus) {
        try {
            Optional<Song> song = songPersistenceService.getSongById(songAnimation.getSongId());

            if (song.isPresent()) {
                song.get().setStatus(songStatus);
                songPersistenceService.saveSong(song.get());
            } else {
                return getJsonResponse(Response.Status.NOT_FOUND, "Song not found.");
            }

            if (songStatus == SongStatus.PUBLISHED) {
                persistRenderedSong(song.get(), songAnimation);
            }

            Integer savedId = songPersistenceService.saveSongAnimation(songAnimation);
            if (savedId == null) {
                return getJsonResponse(Response.Status.NOT_FOUND, "Song animation not found.");
            } else {
                IdResponse idResponse = new IdResponse(savedId);
                return getJsonResponse(idResponse);
            }
        } catch (EntityValidationException e) {
            return getJsonResponse(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return getJsonResponse(Response.Status.INTERNAL_SERVER_ERROR, "An internal error occurred.");
        }
    }

    private int persistSong(Song song) {
        song.setId(null);
        song.setStatus(SongStatus.NEW);
        return songPersistenceService.saveSong(song);
    }

    private String createSongAnimationData() {
        SongAnimationData animationData = new SongAnimationData();

        for (int i = 1; i <= 4; i++) {
            addChannel(animationData, "Roof " + i, "R" + i, 50);
        }

        for (int i = 1; i <= 4; i++) {
            addChannel(animationData, "Pillar " + i, "P" + i, 140);
        }

        return gson.toJson(animationData);
    }

    private void addChannel(SongAnimationData animationData, String channelName, String channelCode, int ledCount) {
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
                .setCode(channelCode)
                .setDisplayOrder(displayOrder)
                .setStartLed(startLed)
                .setEndLed(endLed);
        channels.add(channel);
    }

    private void persistSongAudio(InputStream uploadedInputStream, int songId) throws IOException {
        byte[] bytes = IOUtils.toByteArray(uploadedInputStream);

        SongAudio songAudio = new SongAudio();
        songAudio.setSongId(songId);
        songAudio.setAudioData(bytes);

        songPersistenceService.saveSongAudio(songAudio);
    }

    private void persistSongAnimation(int songId) throws IOException {
        SongAnimation songAnimation = new SongAnimation();
        songAnimation.setSongId(songId);
        songAnimation.setAnimationData(createSongAnimationData());

        songPersistenceService.saveSongAnimation(songAnimation);
    }

    private void persistRenderedSong(Song song, SongAnimation songAnimation) throws IOException {
        RenderedChannelMap renderedChannelMap = new Renderer(song, songAnimation).render();

        RenderedSong renderedSong = new RenderedSong();
        renderedSong.setSongId(song.getId());
        renderedSong.setRenderData(gson.toJson(renderedChannelMap));

        songPersistenceService.saveRenderedSong(renderedSong);
    }
}