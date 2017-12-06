package net.chrisparton.sparkled.rest;

import com.google.gson.Gson;
import net.chrisparton.sparkled.rest.json.GsonProvider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public abstract class RestService {

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
