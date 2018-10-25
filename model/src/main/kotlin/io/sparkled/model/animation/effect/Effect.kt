package io.sparkled.model.animation.effect

import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.fill.Fill
import io.sparkled.model.animation.param.HasParams
import io.sparkled.model.animation.param.Param

import java.util.*

class Effect : HasParams {

    private var uuid: UUID? = null
    private var type: EffectTypeCode? = null
    private var params: List<Param> = ArrayList()
    private var easing: Easing? = null
    private var fill = Fill()
    private var startFrame: Int = 0
    private var endFrame: Int = 0
    private var repetitions = 1
    private var reverse = false

    fun getUuid(): UUID {
        return uuid
    }

    fun setUuid(uuid: UUID): Effect {
        this.uuid = uuid
        return this
    }

    fun getType(): EffectTypeCode {
        return type
    }

    fun setType(type: EffectTypeCode): Effect {
        this.type = type
        return this
    }

    @Override
    fun getParams(): List<Param> {
        return params
    }

    fun setParams(params: List<Param>): Effect {
        this.params = params
        return this
    }

    fun setParams(vararg params: Param): Effect {
        return setParams(Arrays.asList(params))
    }

    fun getEasing(): Easing {
        return easing
    }

    fun setEasing(easing: Easing): Effect {
        this.easing = easing
        return this
    }

    fun getFill(): Fill {
        return fill
    }

    fun setFill(fill: Fill): Effect {
        this.fill = fill
        return this
    }

    fun getStartFrame(): Int {
        return startFrame
    }

    fun setStartFrame(startFrame: Int): Effect {
        this.startFrame = startFrame
        return this
    }

    fun getEndFrame(): Int {
        return endFrame
    }

    fun setEndFrame(endFrame: Int): Effect {
        this.endFrame = endFrame
        return this
    }

    fun getRepetitions(): Int {
        return repetitions
    }

    fun setRepetitions(repetitions: Int): Effect {
        this.repetitions = repetitions
        return this
    }

    fun isReverse(): Boolean {
        return reverse
    }

    fun setReverse(reverse: Boolean): Effect {
        this.reverse = reverse
        return this
    }

    @Override
    fun equals(o: Object?): Boolean {
        if (this === o) return true
        if (o == null || getClass() !== o!!.getClass()) return false
        val effect = o
        return startFrame == effect!!.startFrame &&
                endFrame == effect.endFrame &&
                repetitions == effect.repetitions &&
                reverse == effect.reverse &&
                Objects.equals(uuid, effect.uuid) &&
                type === effect.type &&
                Objects.equals(params, effect.params) &&
                Objects.equals(easing, effect.easing) &&
                Objects.equals(fill, effect.fill)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(uuid, type, params, easing, fill, startFrame, endFrame, repetitions, reverse)
    }

    @Override
    fun toString(): String {
        return "Effect{" +
                "uuid=" + uuid +
                ", type=" + type +
                ", params=" + params +
                ", easing=" + easing +
                ", fill=" + fill +
                ", startFrame=" + startFrame +
                ", endFrame=" + endFrame +
                ", repetitions=" + repetitions +
                ", reverse=" + reverse +
                '}'
    }
}
