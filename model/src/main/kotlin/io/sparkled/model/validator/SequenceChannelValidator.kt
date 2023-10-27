package io.sparkled.model.validator

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.sparkled.model.animation.SequenceChannelEffects
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.util.IdUtils
import io.sparkled.model.validator.exception.EntityValidationException

class SequenceChannelValidator(
    private val objectMapper: ObjectMapper
) {

    fun validate(channel: SequenceChannelModel) {
        if (channel.id == IdUtils.uniqueId()) {
            throw EntityValidationException(String.format(Errors.UUID_MISSING, channel.name))
        } else if (channel.channelJson.isBlank()) {
            throw EntityValidationException(String.format(Errors.CHANNEL_JSON_MISSING, channel.name))
        }

        val effects = getEffectsFromJson(channel)
        validateChannelEffects(channel, effects.effects)
    }

    private fun getEffectsFromJson(channel: SequenceChannelModel): SequenceChannelEffects {
        try {
            return objectMapper.readValue(channel.channelJson)
        } catch (e: JsonProcessingException) {
            throw EntityValidationException(String.format(Errors.CHANNEL_JSON_MALFORMED, channel.name), e)
        }
    }

    private fun validateChannelEffects(channel: SequenceChannelModel, effects: List<Effect>) {
        var previousEndFrame: Int = -1

        for (effect in effects) {
            validateEffect(channel, effect, previousEndFrame)
            previousEndFrame = effect.endFrame
        }
    }

    private fun validateEffect(channel: SequenceChannelModel, effect: Effect, previousEndFrame: Int) {
        val name = channel.name

        when {
            effect.type == "NONE" -> {
                throw EntityValidationException(String.format(Errors.EFFECT_TYPE_MISSING, effect.startFrame, name))
            }
            effect.easing.type == "NONE" -> {
                throw EntityValidationException(String.format(Errors.EFFECT_EASING_TYPE_MISSING, effect.startFrame, name))
            }
            effect.startFrame > effect.endFrame -> {
                throw EntityValidationException(String.format(Errors.EFFECT_BACK_TO_FRONT, effect.startFrame, name))
            }
            effect.startFrame < previousEndFrame -> {
                throw EntityValidationException(String.format(Errors.EFFECT_OVERLAPPING, previousEndFrame, name))
            }
            effect.repetitions <= 0 -> {
                throw EntityValidationException(String.format(Errors.EFFECT_REPETITIONS_INVALID, effect.startFrame, name))
            }
            effect.repetitionSpacing < 0 -> {
                throw EntityValidationException(String.format(Errors.EFFECT_REPETITION_SPACING_INVALID, effect.startFrame, name))
            }
        }
    }

    private object Errors {
        const val UUID_MISSING = "Channel '%s' has no unique identifier."
        const val CHANNEL_JSON_MISSING = "Channel '%s' has no animation data."
        const val CHANNEL_JSON_MALFORMED = "Channel '%s' has malformed effect data."

        const val EFFECT_TYPE_MISSING = "Effect type cannot be empty for effect at frame %d in channel '%s'."
        const val EFFECT_EASING_TYPE_MISSING =
            "EasingFunction type cannot be empty for effect at frame %d in channel '%s'."
        const val EFFECT_BACK_TO_FRONT =
            "Effect start frame cannot be after end frame for effect at frame %d in channel '%s'."
        const val EFFECT_OVERLAPPING = "Overlapping or out-of-order effects detected at frame %d in channel '%s'."
        const val EFFECT_REPETITIONS_INVALID =
            "Effect repetitions cannot be less than 1 for effect at frame %d in channel '%s'."
        const val EFFECT_REPETITION_SPACING_INVALID =
            "Effect repetition spacing cannot be less than 0 for effect at frame %d in channel '%s'."
    }
}
