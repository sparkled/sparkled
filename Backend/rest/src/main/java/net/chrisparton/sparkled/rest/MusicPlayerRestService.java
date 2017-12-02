package net.chrisparton.sparkled.rest;

import net.chrisparton.sparkled.model.entity.ScheduledSong;
import net.chrisparton.sparkled.music.SongPlayerService;
import net.chrisparton.sparkled.persistence.scheduler.ScheduledSongPersistenceService;

import javax.inject.Inject;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.Optional;
import java.util.logging.Logger;

@Path("/musicPlayer")
public class MusicPlayerRestService extends RestService {

    private final Logger logger = Logger.getLogger(MusicPlayerRestService.class.getName());
    private ScheduledSongPersistenceService scheduledSongPersistenceService;
    private SongPlayerService songPlayerService;

    @Inject
    public MusicPlayerRestService(ScheduledSongPersistenceService scheduledSongPersistenceService,
                                  SongPlayerService songPlayerService) {
        this.scheduledSongPersistenceService = scheduledSongPersistenceService;
        this.songPlayerService = songPlayerService;
    }

    @DELETE
    @Path("/currentSong")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stopCurrentSong() {
        logger.info("Stopping current song.");
        songPlayerService.stopCurrentSong();

        Optional<ScheduledSong> currentScheduledSong = scheduledSongPersistenceService.getScheduledSongAtTime(new Date());
        currentScheduledSong.ifPresent(
                scheduledSong -> scheduledSongPersistenceService.removeScheduledSong(scheduledSong.getId())
        );

        return getResponse(Response.Status.OK);
    }
}