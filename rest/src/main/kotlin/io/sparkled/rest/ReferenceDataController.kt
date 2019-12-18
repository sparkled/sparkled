package io.sparkled.rest

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.sparkled.model.reference.AllReferenceData

@Controller("/api/referenceData")
open class ReferenceDataController {

    @Get("/")
    open fun getAllReferenceData(): HttpResponse<Any> {
        return HttpResponse.ok(AllReferenceData)
    }
}
