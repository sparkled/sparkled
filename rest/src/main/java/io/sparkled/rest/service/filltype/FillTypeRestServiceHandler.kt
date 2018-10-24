package io.sparkled.rest.service.filltype;

import io.sparkled.model.animation.fill.reference.FillTypes;
import io.sparkled.rest.service.RestServiceHandler;

import javax.ws.rs.core.Response;

public class FillTypeRestServiceHandler extends RestServiceHandler {

    public Response get() {
        return respondOk(FillTypes.get());
    }
}