package io.sparkled.rest.service.easingtype

import io.sparkled.model.animation.easing.reference.EasingTypes
import io.sparkled.rest.service.RestServiceHandler

import javax.ws.rs.core.Response

open class EasingTypeRestServiceHandler : RestServiceHandler() {

    fun get(): Response {
        return respondOk(EasingTypes.get())
    }
}