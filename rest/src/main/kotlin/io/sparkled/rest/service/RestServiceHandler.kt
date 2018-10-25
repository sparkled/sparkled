package io.sparkled.rest.service

import com.google.gson.Gson
import com.google.inject.persist.Transactional
import io.sparkled.model.util.GsonProvider
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge

import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

/**
 * REST service handlers are used as delegates to handle all REST service logic. This keeps the REST service classes
 * lean and easy to read, which makes it simpler to understand the available endpoints at a glance.
 *
 *
 * The handlers provide another important task: [Transactional] support. The REST service classes are instantiated
 * by [GuiceIntoHK2Bridge], not Guice itself. This means that Guice can't modify their bytecode to support
 * transactions. As dependents of the REST service classes, these handlers don't have that restriction.
 */
abstract class RestServiceHandler {

    protected val gson = GsonProvider.get()

    /**
     * @return A 200 HTTP response with no attached entity.
     */
    protected fun respondOk(): Response {
        return Response.status(Response.Status.OK).build()
    }

    /**
     * @param entity The payload to be returned as JSON.
     * *
     * @return A 200 HTTP response an attached JSON entity.
     */
    protected fun respondOk(entity: Object): Response {
        return respond(Response.Status.OK, entity)
    }

    /**
     * @param status The status of the HTTP response.
     * *
     * @param entity The payload to be returned as JSON.
     * *
     * @return An HTTP response with the specified status and an attached JSON entity.
     */
    protected fun respond(status: Response.Status, entity: Object): Response {
        val responseJson = gson.toJson(entity)

        return Response.status(status)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(responseJson)
                .build()
    }

    /**
     * @param entity    The payload to be returned.
     * *
     * @param mediaType The type of payload to be returned.
     * *
     * @return A 200 HTTP response with the specified status and media type.
     */
    protected fun respondMedia(entity: ByteArray, mediaType: String): Response {
        return Response.status(Response.Status.OK)
                .type(mediaType)
                .entity(entity)
                .build()
    }
}
