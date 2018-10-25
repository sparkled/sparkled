package io.sparkled.rest.service.filltype

import io.sparkled.model.animation.fill.reference.FillTypes
import io.sparkled.rest.service.RestServiceHandler

import javax.ws.rs.core.Response

open class FillTypeRestServiceHandler : RestServiceHandler() {

    fun get(): Response {
        return respondOk(FillTypes.get())
    }
}