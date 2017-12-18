package io.sparkled.rest;

import io.sparkled.model.entity.Song;
import io.sparkled.model.entity.SongAnimation;
import io.sparkled.model.render.RenderedChannelMap;
import io.sparkled.model.validator.SongAnimationValidator;
import io.sparkled.renderer.Renderer;
import io.sparkled.persistence.song.SongPersistenceService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Optional;

@Path("/renderPreview")
public class RenderPreviewRestService extends RestService {

    private final SongPersistenceService songPersistenceService;

    @Inject
    public RenderPreviewRestService(SongPersistenceService songPersistenceService) {
        this.songPersistenceService = songPersistenceService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRenderedSong(@QueryParam("startFrame") int startFrame,
                                    @QueryParam("durationFrames") int durationFrames,
                                    SongAnimation songAnimation) {

        Optional<Song> songOptional = songPersistenceService.getSongById(songAnimation.getSongId());

        if (songOptional.isPresent()) {
            SongAnimationValidator songAnimationValidator = new SongAnimationValidator();
            songAnimationValidator.validate(songAnimation);

            Song song = songOptional.get();
            RenderedChannelMap renderResult = new Renderer(song, songAnimation, startFrame, durationFrames).render();
            return getJsonResponse(renderResult);
        } else {
            return getJsonResponse(Response.Status.NOT_FOUND, "Song not found.");
        }
    }
}