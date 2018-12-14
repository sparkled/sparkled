package io.sparkled.rest.service.setting

import io.sparkled.model.entity.Setting
import javax.inject.Inject
import javax.ws.rs.GET
import javax.ws.rs.PUT
import javax.ws.rs.Path
import javax.ws.rs.PathParam
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/settings")
class SettingRestService
@Inject constructor(private val handler: SettingRestServiceHandler) {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    fun getSettings(): Response {
        return handler.getAllSettings()
    }

    @GET
    @Path("/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    fun getSetting(@PathParam("code") code: String): Response {
        return handler.getSetting(code)
    }

    @PUT
    @Path("/{code}")
    @Produces(MediaType.APPLICATION_JSON)
    fun updateSetting(@PathParam("code") code: String, setting: Setting): Response {
        return handler.updateSetting(code, setting)
    }
}