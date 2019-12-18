package io.sparkled.model.animation.easing

import io.sparkled.model.animation.param.Param
import io.sparkled.model.reference.ReferenceDataItem

data class EasingType(
    override val code: EasingTypeCode = EasingTypeCode.NONE,
    override val name: String = EasingTypeCode.NONE.displayName,
    val params: List<Param> = emptyList()
) : ReferenceDataItem<EasingTypeCode>
