package io.sparkled.rest;

import io.sparkled.model.entity.ScheduledSequence;
import io.sparkled.music.SequencePlayerService;
import io.sparkled.persistence.scheduler.ScheduledSequencePersistenceService;

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
    private ScheduledSequencePersistenceService scheduledSequencePersistenceService;
    private SequencePlayerService sequencePlayerService;

    @Inject
    public MusicPlayerRestService(ScheduledSequencePersistenceService scheduledSequencePersistenceService,
                                  SequencePlayerService sequencePlayerService) {
        this.scheduledSequencePersistenceService = scheduledSequencePersistenceService;
        this.sequencePlayerService = sequencePlayerService;
    }

    @DELETE
    @Path("/currentSequence")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stopCurrentSequence() {
        logger.info("Stopping current sequence.");
        sequencePlayerService.stopCurrentSequence();

        Optional<ScheduledSequence> currentScheduledSequence = scheduledSequencePersistenceService.getScheduledSequenceAtTime(new Date());
        currentScheduledSequence.ifPresent(
                scheduledSequence -> scheduledSequencePersistenceService.removeScheduledSequence(scheduledSequence.getId())
        );

        return getResponse(Response.Status.OK);
    }
}