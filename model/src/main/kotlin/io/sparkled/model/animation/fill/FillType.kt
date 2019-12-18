package io.sparkled.model.animation.fill

import io.sparkled.model.animation.param.Param
import io.sparkled.model.reference.ReferenceDataItem

data class FillType(
    override val code: FillTypeCode = FillTypeCode.NONE,
    override val name: String = FillTypeCode.NONE.displayName,
    val params: List<Param> = emptyList()
) : ReferenceDataItem<FillTypeCode>
