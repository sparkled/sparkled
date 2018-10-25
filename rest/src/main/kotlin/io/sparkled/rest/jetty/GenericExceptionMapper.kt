package io.sparkled.rest.jetty

import io.sparkled.model.validator.exception.EntityValidationException
import io.sparkled.viewmodel.exception.ViewModelConversionException
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.ws.rs.NotAllowedException
import javax.ws.rs.NotFoundException
import javax.ws.rs.core.Context
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider
import java.net.URI

@Provider
class GenericExceptionMapper(@param:Context private val uriInfo: UriInfo) : ExceptionMapper<Throwable> {

    @Override
    fun toResponse(t: Throwable): Response {
        if (t is EntityValidationException || t is ViewModelConversionException) {
            return badRequest(t.getMessage())
        } else if (t is NotFoundException) {
            return notFound()
        } else if (t is NotAllowedException) {
            return notAllowed()
        } else {
            val absolutePath = uriInfo.getAbsolutePath()
            logger.error("Unexpected error response for URI '$absolutePath':", t)
            return internalError()
        }
    }

    private fun badRequest(error: String): Response {
        return Response.status(Response.Status.BAD_REQUEST)
                .location(uriInfo.getRequestUri())
                .entity(error)
                .build()
    }

    private fun notFound(): Response {
        return Response.status(Response.Status.NOT_FOUND)
                .location(uriInfo.getRequestUri())
                .build()
    }

    private fun notAllowed(): Response {
        return Response.status(Response.Status.METHOD_NOT_ALLOWED)
                .location(uriInfo.getRequestUri())
                .build()
    }

    private fun internalError(): Response {
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("An unexpected error occurred.")
                .location(uriInfo.getRequestUri())
                .type(MediaType.APPLICATION_JSON)
                .build()
    }

    companion object {

        private val logger = LoggerFactory.getLogger(GenericExceptionMapper::class.java)
    }
}
