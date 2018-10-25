package io.sparkled.model.animation

import io.sparkled.model.animation.effect.Effect

import java.util.ArrayList
import java.util.Objects

class SequenceChannelEffects {

    private var effects: List<Effect> = ArrayList()

    fun getEffects(): List<Effect> {
        return effects
    }

    fun setEffects(effects: List<Effect>): SequenceChannelEffects {
        this.effects = effects
        return this
    }

    @Override
    fun equals(o: Object): Boolean {
        if (this === o) return true
        if (o !is SequenceChannelEffects) return false
        return Objects.equals(effects, o.effects)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(effects)
    }

    @Override
    fun toString(): String {
        return "SequenceChannelEffects{" +
                "effects=" + effects +
                '}'
    }
}
