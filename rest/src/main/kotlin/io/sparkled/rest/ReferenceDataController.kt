package io.sparkled.rest

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.sparkled.model.animation.easing.reference.EasingTypes
import io.sparkled.model.animation.effect.reference.EffectTypes
import io.sparkled.model.animation.fill.reference.FillTypes

@Controller("/rest")
open class ReferenceDataController {

    @Get("/easingTypes")
    open fun getAllEasingTypes(): HttpResponse<Any> {
        return HttpResponse.ok(EasingTypes.get())
    }

    @Get("/effectTypes")
    open fun getAllEffectTypes(): HttpResponse<Any> {
        return HttpResponse.ok(EffectTypes.get())
    }

    @Get("/fillTypes")
    open fun getAllFillTypes(): HttpResponse<Any> {
        return HttpResponse.ok(FillTypes.get())
    }
}
