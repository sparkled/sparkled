package io.sparkled.model.animation.effect

import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.fill.Fill
import io.sparkled.model.animation.param.Argument
import io.sparkled.model.animation.param.HasArguments
import io.sparkled.model.util.IdUtils
import java.util.UUID

data class Effect(
    var uuid: UUID = IdUtils.NO_UUID,
    var type: EffectTypeCode = EffectTypeCode.NONE,
    var args: List<Argument> = emptyList(),
    var easing: Easing = Easing(),
    var fill: Fill = Fill(),
    var startFrame: Int = 0,
    var endFrame: Int = 0,
    var repetitions: Int = 1,
    var reverse: Boolean = false
) : HasArguments {

    override fun getArguments(): List<Argument> {
        return this.args
    }
}
