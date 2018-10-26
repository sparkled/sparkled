package io.sparkled.rest.service.renderpreview

import io.sparkled.model.entity.SequenceChannel
import javax.inject.Inject
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/renderPreview")
class RenderPreviewRestService @Inject
constructor(private val handler: RenderPreviewRestServiceHandler) {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    fun getRenderedSequence(
        @QueryParam("startFrame") startFrame: Int,
        @QueryParam("frameCount") frameCount: Int,
        sequenceChannels: List<SequenceChannel>
    ): Response {
        return handler.getRenderedSequence(startFrame, frameCount, sequenceChannels)
    }
}