package io.sparkled.model.animation.fill

import io.sparkled.model.animation.param.HasArguments
import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
data class Fill(
    val type: String = "NONE",
    val blendMode: BlendMode = BlendMode.NORMAL,
    override val args: Map<String, List<String>> = emptyMap(),
) : HasArguments
