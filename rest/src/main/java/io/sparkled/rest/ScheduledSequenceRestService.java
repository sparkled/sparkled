package io.sparkled.rest;

import io.sparkled.model.entity.ScheduledSequence;
import io.sparkled.model.entity.Sequence;
import io.sparkled.music.SequenceSchedulerService;
import io.sparkled.persistence.scheduler.ScheduledSequencePersistenceService;
import io.sparkled.viewmodel.ScheduledSequenceViewModel;
import io.sparkled.viewmodel.ScheduledSequenceViewModelConverter;
import org.apache.commons.lang3.time.DateUtils;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import static io.sparkled.model.entity.ScheduledSequence.MIN_SECONDS_BETWEEN_SEQUENCES;

@Path("/scheduledSequences")
public class ScheduledSequenceRestService extends RestService {

    private static final Logger logger = Logger.getLogger(ScheduledSequenceRestService.class.getName());
    private static final SimpleDateFormat DAY_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final SequenceSchedulerService sequenceSchedulerService;
    private final ScheduledSequencePersistenceService scheduledSequencePersistenceService;
    private final ScheduledSequenceViewModelConverter scheduledSequenceViewModelConverter;

    @Inject
    public ScheduledSequenceRestService(SequenceSchedulerService sequenceSchedulerService,
                                        ScheduledSequencePersistenceService scheduledSequencePersistenceService,
                                        ScheduledSequenceViewModelConverter scheduledSequenceViewModelConverter) {
        this.sequenceSchedulerService = sequenceSchedulerService;
        this.scheduledSequencePersistenceService = scheduledSequencePersistenceService;
        this.scheduledSequenceViewModelConverter = scheduledSequenceViewModelConverter;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getScheduledSequencesForDay(@QueryParam("date") String date) {
        if (date == null) {
            String message = "Date must be provided.";
            return getJsonResponse(Response.Status.BAD_REQUEST, message);
        }

        Date parsedDay;
        try {
            parsedDay = DAY_FORMAT.parse(date);
        } catch (ParseException e) {
            String message = "Date format must be '" + DAY_FORMAT.toPattern() + "'.";
            return getJsonResponse(Response.Status.BAD_REQUEST, message);
        }

        Date startDay = DateUtils.truncate(parsedDay, Calendar.DATE);
        Date endDay = DateUtils.addDays(startDay, 1);
        endDay = DateUtils.addMilliseconds(endDay, -1);
        List<ScheduledSequence> scheduledSequences = scheduledSequencePersistenceService.getScheduledSequences(startDay, endDay);
        return getJsonResponse(scheduledSequences);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response scheduleSequence(ScheduledSequenceViewModel viewModel) {
        ScheduledSequence scheduledSequence = scheduledSequenceViewModelConverter.fromViewModel(viewModel);

        Date startTime = scheduledSequence.getStartTime();
        Date earliestStartTime = DateUtils.addSeconds(new Date(), MIN_SECONDS_BETWEEN_SEQUENCES);
        if (startTime == null || startTime.before(earliestStartTime)) {
            String message = "Sequences must be scheduled at least " + MIN_SECONDS_BETWEEN_SEQUENCES + " seconds in the future.";
            return getJsonResponse(Response.Status.BAD_REQUEST, message);
        }

        Sequence sequence = scheduledSequence.getSequence();
        if (sequence != null) {
            Date offsetStartTime = DateUtils.addSeconds(startTime, -MIN_SECONDS_BETWEEN_SEQUENCES);
            int seconds = (int) Math.ceil(sequence.getDurationFrames() / sequence.getFramesPerSecond());
            seconds += MIN_SECONDS_BETWEEN_SEQUENCES;
            Date endTime = DateUtils.addSeconds(offsetStartTime, seconds);
            List<ScheduledSequence> overlappingSequences = scheduledSequencePersistenceService.getScheduledSequences(offsetStartTime, endTime);

            if (!overlappingSequences.isEmpty()) {
                String message = "Scheduled sequences cannot overlap, and must have at least a " + MIN_SECONDS_BETWEEN_SEQUENCES + " second gap between them.";
                return getJsonResponse(Response.Status.BAD_REQUEST, message);
            }
        }

        boolean success = scheduledSequencePersistenceService.saveScheduledSequence(scheduledSequence);
        if (success) {
            logger.info("Sequence scheduled: " + scheduledSequence.getSequence().getName());
            sequenceSchedulerService.scheduleNextSequence();
        }
        return getResponse(success ? Response.Status.OK : Response.Status.BAD_REQUEST);
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response unscheduleSequence(@PathParam("id") int id) {
        boolean success = scheduledSequencePersistenceService.removeScheduledSequence(id);
        return getResponse(success ? Response.Status.OK : Response.Status.BAD_REQUEST);
    }
}