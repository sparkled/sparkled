package net.chrisparton.sparkled.rest;

import net.chrisparton.sparkled.model.entity.Song;
import net.chrisparton.sparkled.model.entity.SongAnimation;
import net.chrisparton.sparkled.model.validator.SongAnimationValidator;
import net.chrisparton.sparkled.persistence.song.SongPersistenceService;
import net.chrisparton.sparkled.model.render.RenderedChannelMap;
import net.chrisparton.sparkled.renderer.Renderer;

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