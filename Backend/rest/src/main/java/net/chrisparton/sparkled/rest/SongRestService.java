package net.chrisparton.sparkled.rest;

import net.chrisparton.sparkled.entity.*;
import net.chrisparton.sparkled.persistence.song.SongPersistenceService;
import net.chrisparton.sparkled.preprocessor.EntityValidationException;
import net.chrisparton.sparkled.preprocessor.SongPreprocessor;
import net.chrisparton.sparkled.renderer.Renderer;
import net.chrisparton.sparkled.renderer.data.RenderedChannel;
import net.chrisparton.sparkled.rest.response.IdResponse;
import org.apache.commons.io.IOUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Path("/song")
public class SongRestService extends RestService {

    private static final String MP3_MIME_TYPE = "audio/mpeg";
    private SongPersistenceService persistenceService = new SongPersistenceService();

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getSong(@PathParam("id") int id) {
        Optional<Song> song = persistenceService.getSongById(id);

        if (song.isPresent()) {
            return getJsonResponse(song.get());
        }

        return getJsonResponse(Response.Status.NOT_FOUND, "Failed to find song.");
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSongs() {
        List<Song> songs = persistenceService.getAllSongs();
        return getJsonResponse(songs);
    }

    @GET
    @Path("/data/{id}")
    @Produces(MP3_MIME_TYPE)
    public Response getSongData(@PathParam("id") int id) {
        Optional<SongData> songData = persistenceService.getSongDataById(id);

        if (songData.isPresent()) {
            return getBinaryResponse(songData.get().getMp3Data(), MP3_MIME_TYPE);
        }

        return getResponse(Response.Status.NOT_FOUND);
    }

    @GET
    @Path("/render/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRenderedSong(@PathParam("id") int id,
                                    @QueryParam("durationFrames") int durationFrames,
                                    @QueryParam("startFrame") int startFrame) {
        Optional<Song> song = persistenceService.getSongById(id);

        if (song.isPresent()) {
            Map<String, RenderedChannel> renderedChannels = renderSong(song.get(), startFrame, durationFrames);
            return getJsonResponse(renderedChannels);
        }

        return getJsonResponse(Response.Status.NOT_FOUND, "Song not found.");
    }

    @PUT
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response putSong(@FormDataParam("song") String songJson,
                            @FormDataParam("mp3") InputStream uploadedInputStream,
                            @FormDataParam("mp3") FormDataContentDisposition fileDetail) {

        try {
            int songId = persistSong(songJson);
            persistSongData(uploadedInputStream, songId);

            IdResponse idResponse = new IdResponse(songId);
            return getJsonResponse(idResponse);
        } catch (IOException e) {
            return getResponse(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeSong(@PathParam("id") int id) {
        boolean success = persistenceService.removeSongAndData(id);
        return getResponse(
                success ? Response.Status.OK : Response.Status.NOT_MODIFIED
        );
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateSong(String songJson) {
        Song updatedSong = gson.fromJson(songJson, Song.class);

        try {
            Optional<Song> songOptional = persistenceService.getSongById(updatedSong.getId());
            if (songOptional.isPresent()) {
                Song song = songOptional.get();
                song.setAnimationData(updatedSong.getAnimationData());

                int songId = saveSong(song);
                IdResponse idResponse = new IdResponse(songId);
                return getJsonResponse(idResponse);
            }

            return getJsonResponse(Response.Status.NOT_FOUND, "Song not found.");
        } catch (EntityValidationException e) {
            return getJsonResponse(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return getJsonResponse(Response.Status.INTERNAL_SERVER_ERROR, "An internal error occurred.");
        }
    }

    private int persistSong(String songJson) {
        Song song = gson.fromJson(songJson, Song.class);
        song.setId(null);
        if (song.getAlbum() == null) {
            song.setAlbum("No Album");
        }
        song.setAnimationData(gson.toJson(createSongAnimationData()));
        return saveSong(song);
    }

    private int saveSong(Song song) {
        SongPreprocessor preprocessor = new SongPreprocessor(song);
        preprocessor.validate();
        preprocessor.escapeText();

        Renderer renderer = new Renderer(song, 0, song.getDurationFrames());
        Map<String, RenderedChannel> renderedChannels = renderer.render();
        String renderJson = gson.toJson(renderedChannels);
        RenderedSong renderedSong = persistenceService.getRenderedSongById(song.getId()).orElse(new RenderedSong());
        renderedSong.setRenderData(renderJson);

        return persistenceService.saveSong(song, renderedSong);
    }

    private SongAnimationData createSongAnimationData() {
        SongAnimationData animationData = new SongAnimationData();

        for (int i = 1; i <= 4; i++) {
            addChannel(animationData, "Pillar " + i, "P" + i, 50);
        }

        for (int i = 1; i <= 8; i++) {
            addChannel(animationData, "Arch " + i, "A" + i, 16);
        }

        return animationData;
    }

    private void addChannel(SongAnimationData animationData, String channelName, String channelCode, int ledCount) {
        int startLed = 0;
        int displayOrder = 0;

        List<AnimationEffectChannel> channels = animationData.getChannels();
        if (!channels.isEmpty()) {
            AnimationEffectChannel lastChannel = channels.get(channels.size() - 1);
            startLed = lastChannel.getEndLed() + 1;
            displayOrder = lastChannel.getDisplayOrder() + 1;
        }

        int endLed = startLed + ledCount - 1;
        AnimationEffectChannel channel = new AnimationEffectChannel(channelName, channelCode, displayOrder, startLed, endLed);
        channels.add(channel);
    }

    private void persistSongData(InputStream uploadedInputStream, int songId) throws IOException {
        byte[] bytes = IOUtils.toByteArray(uploadedInputStream);

        SongData songData = new SongData();
        songData.setSongId(songId);
        songData.setMp3Data(bytes);

        persistenceService.saveSongData(songData);
    }

    private Map<String, RenderedChannel> renderSong(Song song, int startFrame, int durationFrames) {
        Renderer renderer = new Renderer(song, startFrame, durationFrames);
        return renderer.render();
    }
}