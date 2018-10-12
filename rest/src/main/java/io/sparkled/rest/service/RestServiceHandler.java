package io.sparkled.rest.service;

import com.google.gson.Gson;
import com.google.inject.persist.Transactional;
import io.sparkled.rest.json.GsonProvider;
import org.jvnet.hk2.guice.bridge.api.GuiceIntoHK2Bridge;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST service handlers are used as delegates to handle all REST service logic. This keeps the REST service classes
 * lean and easy to read, which makes it simpler to understand the available endpoints at a glance.
 * <p>
 * The handlers provide another important task: {@link Transactional} support. The REST service classes are instantiated
 * by {@link GuiceIntoHK2Bridge}, not Guice itself. This means that Guice can't modify their bytecode to support
 * transactions. As dependents of the REST service classes, these handlers don't have that restriction.
 */
public abstract class RestServiceHandler {

    protected final Gson gson = GsonProvider.get();

    protected Response getJsonResponse(Object responseObject) {
        return getJsonResponse(Response.Status.OK, responseObject);
    }

    protected Response getJsonResponse(Response.Status status, Object responseObject) {
        String responseJson = gson.toJson(responseObject);

        return Response.status(status)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(responseJson)
                .build();
    }

    protected Response getBinaryResponse(Object responseObject, String mediaType) {
        return Response.status(Response.Status.OK)
                .type(mediaType)
                .entity(responseObject)
                .build();
    }

    protected Response getResponse(Response.Status status) {
        return Response.status(status).build();
    }
}
