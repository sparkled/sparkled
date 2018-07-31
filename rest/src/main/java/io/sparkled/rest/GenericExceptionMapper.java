package io.sparkled.rest;

import javax.ws.rs.NotAllowedException;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger logger = Logger.getLogger(GenericExceptionMapper.class.getName());
    private final UriInfo uriInfo;

    public GenericExceptionMapper(@Context UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }

    @Override
    public Response toResponse(Throwable t) {
        if (t instanceof NotFoundException) {
            return notFound();
        } else if (t instanceof NotAllowedException) {
            return notAllowed();
        } else {
            URI absolutePath = uriInfo.getAbsolutePath();
            logger.log(Level.SEVERE, "Unexpected error response for URI '" + absolutePath + "':", t);
            return internalError();
        }
    }

    private Response notFound() {
        return Response.status(Response.Status.NOT_FOUND)
                .location(uriInfo.getRequestUri())
                .build();
    }

    private Response notAllowed() {
        return Response.status(Response.Status.METHOD_NOT_ALLOWED)
                .location(uriInfo.getRequestUri())
                .build();
    }

    private Response internalError() {
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("An unexpected error occurred.")
                .location(uriInfo.getRequestUri())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
