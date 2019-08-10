package io.sparkled.model.animation

import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.model.animation.effect.Effect

data class SequenceChannelEffects(val effects: List<Effect> = emptyList()) {

    companion object {
        val EMPTY_JSON = ObjectMapper().writeValueAsString(SequenceChannelEffects())!!
    }
}
