package io.sparkled.model.animation.effect

import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.fill.Fill
import io.sparkled.model.animation.param.HasParams
import io.sparkled.model.animation.param.Param
import io.sparkled.model.util.IdUtils
import java.util.UUID

data class Effect(
    var uuid: UUID = IdUtils.NO_UUID,
    var type: EffectTypeCode = EffectTypeCode.NONE,
    private var params: List<Param> = listOf(),
    var easing: Easing = Easing(),
    var fill: Fill = Fill(),
    var startFrame: Int = 0,
    var endFrame: Int = 0,
    var repetitions: Int = 1,
    var reverse: Boolean = false
) : HasParams {

    override fun getParams(): List<Param> {
        return this.params
    }
}
