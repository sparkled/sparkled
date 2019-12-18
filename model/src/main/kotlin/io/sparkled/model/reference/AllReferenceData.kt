package io.sparkled.model.reference

import io.sparkled.model.animation.easing.reference.EasingTypes
import io.sparkled.model.animation.effect.reference.EffectTypes
import io.sparkled.model.animation.fill.BlendMode
import io.sparkled.model.animation.fill.reference.FillTypes

object AllReferenceData {
    val blendModes = BlendMode.values().map { SimpleReferenceDataItem(it, it.displayName) }
    val easingTypes = EasingTypes.get()
    val effectTypes = EffectTypes.get()
    val fillTypes = FillTypes.get()
}
