package io.sparkled.rest.service.filltype

import io.sparkled.model.animation.fill.reference.FillTypes
import io.sparkled.rest.service.RestServiceHandler

import javax.ws.rs.core.Response

class FillTypeRestServiceHandler : RestServiceHandler() {

    fun get(): Response {
        return respondOk(FillTypes.get())
    }
}