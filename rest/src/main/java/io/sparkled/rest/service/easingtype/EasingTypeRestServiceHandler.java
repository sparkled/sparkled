package io.sparkled.rest.service.easingtype;

import io.sparkled.model.animation.easing.reference.EasingTypes;
import io.sparkled.rest.service.RestServiceHandler;

import javax.ws.rs.core.Response;

public class EasingTypeRestServiceHandler extends RestServiceHandler {

    public Response get() {
        return getJsonResponse(EasingTypes.get());
    }
}