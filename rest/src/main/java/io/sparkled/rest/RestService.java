package io.sparkled.rest;

import com.google.gson.Gson;
import io.sparkled.rest.json.GsonProvider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public abstract class RestService {

    final Gson gson = GsonProvider.get();

    Response getJsonResponse(Object responseObject) {
        return getJsonResponse(Response.Status.OK, responseObject);
    }

    Response getJsonResponse(Response.Status status, Object responseObject) {
        String responseJson = gson.toJson(responseObject);

        return Response.status(status)
                .type(MediaType.APPLICATION_JSON_TYPE)
                .entity(responseJson)
                .build();
    }

    Response getBinaryResponse(Object responseObject, String mediaType) {
        return Response.status(Response.Status.OK)
                .type(mediaType)
                .entity(responseObject)
                .build();
    }

    Response getResponse(Response.Status status) {
        return Response.status(status).build();
    }
}
