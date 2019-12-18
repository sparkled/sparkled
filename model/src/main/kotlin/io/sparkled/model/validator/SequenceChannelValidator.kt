package io.sparkled.model.validator

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.sparkled.model.animation.SequenceChannelEffects
import io.sparkled.model.animation.easing.EasingTypeCode
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.animation.effect.EffectTypeCode
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.model.validator.exception.EntityValidationException

class SequenceChannelValidator(
    private val objectMapper: ObjectMapper
) {

    @Throws(EntityValidationException::class)
    fun validate(channel: SequenceChannel) {
        val channelJson = channel.getChannelJson()

        if (channel.getUuid() == null) {
            throw EntityValidationException(String.format(Errors.UUID_MISSING, channel.getName()))
        } else if (channelJson == null) {
            throw EntityValidationException(String.format(Errors.CHANNEL_JSON_MISSING, channel.getName()))
        }

        val effects = getEffectsFromJson(channel)
        validateChannelEffects(channel, effects.effects)
    }

    private fun getEffectsFromJson(channel: SequenceChannel): SequenceChannelEffects {
        try {
            return objectMapper.readValue(channel.getChannelJson(), SequenceChannelEffects::class.java)
        } catch (e: JsonProcessingException) {
            throw EntityValidationException(String.format(Errors.CHANNEL_JSON_MALFORMED, channel.getName()), e)
        }
    }

    private fun validateChannelEffects(channel: SequenceChannel, effects: List<Effect>) {
        var previousEndFrame: Int = -1

        for (effect in effects) {
            validateEffect(channel, effect, previousEndFrame)
            previousEndFrame = effect.endFrame
        }
    }

    private fun validateEffect(channel: SequenceChannel, effect: Effect, previousEndFrame: Int) {
        if (effect.type === EffectTypeCode.NONE) {
            throw EntityValidationException(String.format(Errors.EFFECT_TYPE_MISSING, effect.startFrame, channel.getName()))
        }

        if (effect.easing.type === EasingTypeCode.NONE) {
            throw EntityValidationException(String.format(Errors.EFFECT_EASING_TYPE_MISSING, effect.startFrame, channel.getName()))
        }

        if (effect.startFrame > effect.endFrame) {
            throw EntityValidationException(String.format(Errors.EFFECT_BACK_TO_FRONT, effect.startFrame, channel.getName()))
        }

        if (effect.startFrame < previousEndFrame) {
            throw EntityValidationException(String.format(Errors.EFFECT_OVERLAPPING, previousEndFrame, channel.getName()))
        }

        if (effect.repetitions <= 0) {
            throw EntityValidationException(String.format(Errors.EFFECT_REPETITIONS_INVALID, effect.startFrame, channel.getName()))
        }

        if (effect.repetitionSpacing < 0) {
            throw EntityValidationException(String.format(Errors.EFFECT_REPETITION_SPACING_INVALID, effect.startFrame, channel.getName()))
        }
    }

    private object Errors {
        internal const val UUID_MISSING = "Channel '%s' has no unique identifier."
        internal const val CHANNEL_JSON_MISSING = "Channel '%s' has no animation data."
        internal const val CHANNEL_JSON_MALFORMED = "Channel '%s' has malformed effect data."

        internal const val EFFECT_TYPE_MISSING = "Effect type cannot be empty for effect at frame %d in channel '%s'."
        internal const val EFFECT_EASING_TYPE_MISSING =
            "EasingFunction type cannot be empty for effect at frame %d in channel '%s'."
        internal const val EFFECT_BACK_TO_FRONT =
            "Effect start frame cannot be after end frame for effect at frame %d in channel '%s'."
        internal const val EFFECT_OVERLAPPING = "Overlapping or out-of-order effects detected at frame %d in channel '%s'."
        internal const val EFFECT_REPETITIONS_INVALID =
            "Effect repetitions cannot be less than 1 for effect at frame %d in channel '%s'."
        internal const val EFFECT_REPETITION_SPACING_INVALID =
            "Effect repetition spacing cannot be less than 0 for effect at frame %d in channel '%s'."
    }
}
