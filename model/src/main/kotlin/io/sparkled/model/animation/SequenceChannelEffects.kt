package io.sparkled.model.animation

import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.util.GsonProvider

data class SequenceChannelEffects(val effects: List<Effect> = emptyList()) {

    companion object {
        val EMPTY_JSON = GsonProvider.get().toJson(SequenceChannelEffects())!!
    }
}
