package io.sparkled.rest.service.effecttype;

import io.sparkled.model.animation.effect.reference.EffectTypes;
import io.sparkled.rest.service.RestServiceHandler;

import javax.ws.rs.core.Response;

public class EffectTypeRestServiceHandler extends RestServiceHandler {

    public Response get() {
        return respondOk(EffectTypes.get());
    }
}