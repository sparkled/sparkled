package io.sparkled.rest.service.filltype

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/fillTypes")
class FillTypeRestService
@Inject constructor(private val handler: FillTypeRestServiceHandler) {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun get(): Response {
        return handler.get()
    }
}