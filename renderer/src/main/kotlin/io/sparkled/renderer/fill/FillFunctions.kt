package io.sparkled.renderer.fill

import io.sparkled.model.animation.fill.FillTypeCode
import io.sparkled.renderer.fill.function.RainbowFill
import io.sparkled.renderer.fill.function.SolidFill

import java.util.HashMap

object FillFunctions {

    private val functions = HashMap()

    init {
        functions.put(FillTypeCode.RAINBOW, RainbowFill())
        functions.put(FillTypeCode.SOLID, SolidFill())
    }

    operator fun get(fillType: FillTypeCode): FillFunction {
        return functions.get(fillType)
    }
}