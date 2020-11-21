package io.sparkled.model.animation.easing

import io.sparkled.model.animation.param.Param

data class EditorItem(
    val code: String = "NONE",
    val name: String = "None",
    val params: List<Param> = emptyList()
)
