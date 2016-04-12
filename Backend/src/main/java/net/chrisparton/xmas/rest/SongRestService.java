package net.chrisparton.xmas.rest;

import com.google.gson.Gson;
import com.sun.jersey.core.header.FormDataContentDisposition;
import com.sun.jersey.multipart.FormDataParam;
import net.chrisparton.xmas.entity.Song;
import net.chrisparton.xmas.entity.SongAnimationData;
import net.chrisparton.xmas.entity.SongData;
import net.chrisparton.xmas.persistence.song.SongPersistenceService;
import net.chrisparton.xmas.preprocessor.EntityValidationException;
import net.chrisparton.xmas.rest.response.IdResponse;
import org.apache.commons.io.IOUtils;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
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

        return getResponse(Response.Status.NOT_FOUND);
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

            return getResponse(Response.Status.NOT_FOUND);
        } catch (EntityValidationException e) {
            return getResponse(Response.Status.BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            return getResponse(Response.Status.INTERNAL_SERVER_ERROR);
        }
    }

    private int persistSong(String songJson) {
        Song song = new Gson().fromJson(songJson, Song.class);
        song.setId(null);
        song.setAnimationData(gson.toJson(new SongAnimationData()));
        return persistenceService.saveSong(song);
    }

    private void persistSongData(InputStream uploadedInputStream, int songId) throws IOException {
        byte[] bytes = IOUtils.toByteArray(uploadedInputStream);

        SongData songData = new SongData();
        songData.setSongId(songId);
        songData.setMp3Data(bytes);

        persistenceService.saveSongData(songData);
    }
}