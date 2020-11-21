package io.sparkled.model.animation.effect

import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.fill.Fill
import io.sparkled.model.animation.param.HasArguments
import io.sparkled.model.util.IdUtils
import java.util.*

data class Effect(
    var uuid: UUID = IdUtils.NO_UUID,
    var type: String = "NONE",
    var easing: Easing = Easing("LINEAR"),
    var fill: Fill = Fill(),
    var startFrame: Int = 0,
    var endFrame: Int = 0,
    var repetitions: Int = 1,
    var repetitionSpacing: Int = 0,

    override val args: Map<String, List<String>> = emptyMap()
) : HasArguments
