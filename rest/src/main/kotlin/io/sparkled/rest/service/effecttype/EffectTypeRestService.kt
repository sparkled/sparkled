package io.sparkled.rest.service.effecttype

import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/effectTypes")
class EffectTypeRestService
@Inject constructor(private val handler: EffectTypeRestServiceHandler) {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun get(): Response {
        return handler.get()
    }
}