package io.sparkled.model.animation.effect

import io.sparkled.model.animation.easing.Easing
import io.sparkled.model.animation.fill.Fill
import io.sparkled.model.animation.param.HasParams
import io.sparkled.model.animation.param.Param
import java.util.ArrayList
import java.util.UUID

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

    fun getUuid(): UUID? {
        return uuid
    }

    fun setUuid(uuid: UUID): Effect {
        this.uuid = uuid
        return this
    }

    fun getType(): EffectTypeCode? {
        return type
    }

    fun setType(type: EffectTypeCode): Effect {
        this.type = type
        return this
    }

    override fun getParams(): List<Param> {
        return this.params
    }

    fun setParams(params: List<Param>): Effect {
        this.params = params
        return this
    }

    fun getEasing(): Easing? {
        return easing
    }

    fun setEasing(easing: Easing): Effect {
        this.easing = easing
        return this
    }

    fun getFill(): Fill? {
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Effect

        if (uuid != other.uuid) return false
        if (type != other.type) return false
        if (params != other.params) return false
        if (easing != other.easing) return false
        if (fill != other.fill) return false
        if (startFrame != other.startFrame) return false
        if (endFrame != other.endFrame) return false
        if (repetitions != other.repetitions) return false
        if (reverse != other.reverse) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uuid?.hashCode() ?: 0
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + params.hashCode()
        result = 31 * result + (easing?.hashCode() ?: 0)
        result = 31 * result + fill.hashCode()
        result = 31 * result + startFrame
        result = 31 * result + endFrame
        result = 31 * result + repetitions
        result = 31 * result + reverse.hashCode()
        return result
    }

    override fun toString(): String {
        return "Effect(uuid=$uuid, type=$type, params=$params, easing=$easing, fill=$fill, startFrame=$startFrame, endFrame=$endFrame, repetitions=$repetitions, reverse=$reverse)"
    }
}
