package io.sparkled.renderer.util

import io.sparkled.model.animation.effect.EffectTypeCode
import io.sparkled.renderer.effect.EffectRenderer
import io.sparkled.renderer.effect.FlashEffectRenderer
import io.sparkled.renderer.effect.LineEffectRenderer
import io.sparkled.renderer.effect.SplitLineEffectRenderer

import java.util.HashMap
import java.util.function.Supplier

object EffectTypeRenderers {

    private val RENDERERS = HashMap()

    init {
        RENDERERS.put(EffectTypeCode.FLASH, ???({ FlashEffectRenderer() }))
        RENDERERS.put(EffectTypeCode.LINE, ???({ LineEffectRenderer() }))
        RENDERERS.put(EffectTypeCode.SPLIT_LINE, ???({ SplitLineEffectRenderer() }))
    }

    operator fun get(code: EffectTypeCode): EffectRenderer {
        val supplier = RENDERERS.get(code)
        return supplier.get()
    }
}
