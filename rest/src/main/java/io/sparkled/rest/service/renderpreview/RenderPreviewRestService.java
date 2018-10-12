package io.sparkled.rest.service.renderpreview;

import io.sparkled.model.entity.SequenceChannel;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/renderPreview")
public class RenderPreviewRestService {

    private final RenderPreviewRestServiceHandler handler;

    @Inject
    public RenderPreviewRestService(RenderPreviewRestServiceHandler handler) {
        this.handler = handler;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getRenderedSequence(@QueryParam("startFrame") int startFrame,
                                        @QueryParam("durationFrames") int durationFrames,
                                        List<SequenceChannel> sequenceChannels) {
        return handler.getRenderedSequence(startFrame, durationFrames, sequenceChannels);
    }
}