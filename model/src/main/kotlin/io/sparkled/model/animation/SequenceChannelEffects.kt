package io.sparkled.model.animation

import io.sparkled.model.animation.effect.Effect

data class SequenceChannelEffects(val effects: List<Effect> = emptyList()) {

    companion object {
        const val EMPTY_JSON = "[]"
    }
}
