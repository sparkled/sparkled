package net.chrisparton.sparkled.rest;

import com.google.gson.Gson;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import net.chrisparton.sparkled.entity.AnimationEffectChannel;
import net.chrisparton.sparkled.entity.Song;
import net.chrisparton.sparkled.entity.SongAnimationData;
import net.chrisparton.sparkled.entity.SongData;
import net.chrisparton.sparkled.persistence.song.SongPersistenceService;
import net.chrisparton.sparkled.preprocessor.EntityValidationException;
import net.chrisparton.sparkled.renderer.Renderer;
import net.chrisparton.sparkled.renderer.data.AnimationFrame;
import net.chrisparton.sparkled.rest.response.IdResponse;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Path("/song")
public class SongRestService extends RestService {

    private static final String MP3_MIME_TYPE = "audio/mpeg";
    private SongPersistenceService persistenceService = new SongPersistenceService();
    private Gson gson = new Gson();

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
                                    @QueryParam("duration-seconds") int durationSeconds,
                                    @QueryParam("start-frame") int startFrame) {
        Optional<Song> song = persistenceService.getSongById(id);

        if (song.isPresent()) {
            Renderer renderer = new Renderer(song.get(), startFrame, durationSeconds);
            List<AnimationFrame> animationFrames = renderer.render();

            return getJsonResponse(animationFrames);
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
                persistenceService.saveSong(song);

                IdResponse idResponse = new IdResponse(song.getId());
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
        song.setAnimationData(gson.toJson(createSongAnimationData()));
        return persistenceService.saveSong(song);
    }

    private SongAnimationData createSongAnimationData() {
        SongAnimationData animationData = new SongAnimationData();
        animationData.getChannels().addAll(Arrays.asList(
                new AnimationEffectChannel("Pillar 1", 0, 148, 197),
                new AnimationEffectChannel("Pillar 2", 1, 198, 247),
                new AnimationEffectChannel("Pillar 3", 2, 248, 297),
                new AnimationEffectChannel("Pillar 4", 3, 298, 347),
                new AnimationEffectChannel("Arch 1", 4, 0, 36),
                new AnimationEffectChannel("Arch 2", 5, 37, 73),
                new AnimationEffectChannel("Arch 3", 6, 74, 110),
                new AnimationEffectChannel("Arch 4", 7, 111, 147),
                new AnimationEffectChannel("Global", 8, 0, 347)
        ));
        return animationData;
    }

    private void persistSongData(InputStream uploadedInputStream, int songId) throws IOException {
        byte[] bytes = IOUtils.toByteArray(uploadedInputStream);

        SongData songData = new SongData();
        songData.setSongId(songId);
        songData.setMp3Data(bytes);

        persistenceService.saveSongData(songData);
    }
}