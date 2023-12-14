package io.sparkled.model.animation.effect

import io.sparkled.model.UniqueId
import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.fill.Fill
import io.sparkled.model.animation.param.HasArguments
import io.sparkled.model.annotation.GenerateClientType
import io.sparkled.model.util.IdUtils.uniqueId

@GenerateClientType
data class Effect(
    val id: UniqueId = uniqueId(),
    val type: String = "NONE",
    val easing: Easing = Easing("LINEAR"),
    val fill: Fill = Fill(),
    val startFrame: Int = 0,
    val endFrame: Int = 0,
    val repetitions: Int = 1,
    val repetitionSpacing: Int = 0,

    /**
     * Render this effect only on the pixels specified in this list. If the list is empty, render for all pixels.
     */
    val targetPixels: MutableSet<Int> = mutableSetOf(),

    override val args: Map<String, List<String>> = emptyMap(),
) : HasArguments
