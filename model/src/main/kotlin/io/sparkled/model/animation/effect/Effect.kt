package io.sparkled.model.animation.effect

import com.fasterxml.jackson.annotation.JsonIgnore
import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.fill.Fill
import io.sparkled.model.animation.param.HasArguments
import io.sparkled.model.animation.param.ParamCode
import io.sparkled.model.util.IdUtils
import java.util.UUID

data class Effect(
    var uuid: UUID = IdUtils.NO_UUID,
    var type: EffectTypeCode = EffectTypeCode.NONE,
    var args: Map<ParamCode, List<String>> = emptyMap(),
    var easing: Easing = Easing(),
    var fill: Fill = Fill(),
    var startFrame: Int = 0,
    var endFrame: Int = 0,
    var repetitions: Int = 1,
    var repetitionSpacing: Int = 0
) : HasArguments {

    @JsonIgnore
    override fun getArguments(): Map<ParamCode, List<String>> {
        return this.args
    }
}
