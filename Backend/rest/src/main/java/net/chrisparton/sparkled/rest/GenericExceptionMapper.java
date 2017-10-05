package net.chrisparton.sparkled.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
    private UriInfo uriInfo;

    public GenericExceptionMapper(@Context UriInfo uriInfo) {
        this.uriInfo = uriInfo;
    }
    @Override
    public Response toResponse(Throwable t) {
        return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("An unexpected error occurred.")
                .location(uriInfo.getRequestUri())
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
