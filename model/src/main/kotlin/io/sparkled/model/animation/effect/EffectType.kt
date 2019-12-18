package io.sparkled.model.animation.effect

import io.sparkled.model.animation.param.Param
import io.sparkled.model.reference.ReferenceDataItem

data class EffectType(
    override val code: EffectTypeCode = EffectTypeCode.NONE,
    override val name: String = EffectTypeCode.NONE.displayName,
    val params: List<Param> = emptyList()
) : ReferenceDataItem<EffectTypeCode>
