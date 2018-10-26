package io.sparkled.model.animation

import io.sparkled.model.animation.effect.Effect

class SequenceChannelEffects {

    private var effects: List<Effect> = ArrayList()

    fun getEffects(): List<Effect> {
        return effects
    }

    fun setEffects(effects: List<Effect>): SequenceChannelEffects {
        this.effects = effects
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SequenceChannelEffects

        if (effects != other.effects) return false

        return true
    }

    override fun hashCode(): Int {
        return effects.hashCode()
    }

    override fun toString(): String {
        return "SequenceChannelEffects(effects=$effects)"
    }
}
