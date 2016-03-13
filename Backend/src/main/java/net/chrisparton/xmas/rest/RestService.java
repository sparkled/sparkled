package net.chrisparton.xmas.rest;

import com.google.gson.Gson;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public abstract class RestService {

    protected Response getJsonResponse(Object responseObject) {
        String responseJson = new Gson().toJson(responseObject);

        return Response.status(Response.Status.OK)
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
