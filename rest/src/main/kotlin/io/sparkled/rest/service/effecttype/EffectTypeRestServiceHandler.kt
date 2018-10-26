package io.sparkled.rest.service.effecttype

import io.sparkled.model.animation.effect.reference.EffectTypes
import io.sparkled.rest.service.RestServiceHandler

import javax.ws.rs.core.Response

open class EffectTypeRestServiceHandler : RestServiceHandler() {

    fun get(): Response {
        return respondOk(EffectTypes.get())
    }
}