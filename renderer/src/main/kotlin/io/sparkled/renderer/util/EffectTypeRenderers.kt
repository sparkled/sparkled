package io.sparkled.renderer.util

import io.sparkled.model.animation.effect.EffectTypeCode
import io.sparkled.renderer.effect.EffectRenderer
import io.sparkled.renderer.effect.FlashEffectRenderer
import io.sparkled.renderer.effect.LineEffectRenderer
import io.sparkled.renderer.effect.SplitLineEffectRenderer
import java.util.HashMap

object EffectTypeRenderers {

    private val RENDERERS = HashMap<EffectTypeCode, EffectRenderer>()

    init {
        RENDERERS[EffectTypeCode.FLASH] = FlashEffectRenderer()
        RENDERERS[EffectTypeCode.LINE] = LineEffectRenderer()
        RENDERERS[EffectTypeCode.SPLIT_LINE] = SplitLineEffectRenderer()
    }

    operator fun get(code: EffectTypeCode): EffectRenderer {
        return RENDERERS[code]!!
    }
}
