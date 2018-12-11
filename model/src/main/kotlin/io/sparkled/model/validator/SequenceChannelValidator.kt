package io.sparkled.model.validator

import com.google.gson.JsonSyntaxException
import io.sparkled.model.animation.SequenceChannelEffects
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.animation.effect.EffectTypeCode
import io.sparkled.model.animation.param.Argument
import io.sparkled.model.animation.param.ParamType
import io.sparkled.model.entity.SequenceChannel
import io.sparkled.model.util.GsonProvider
import io.sparkled.model.validator.exception.EntityValidationException

class SequenceChannelValidator {

    @Throws(EntityValidationException::class)
    fun validate(channel: SequenceChannel) {
        val channelJson = channel.getChannelJson()

        if (channel.getUuid() == null) {
            throw EntityValidationException(Errors.UUID_MISSING)
        } else if (channelJson == null) {
            throw EntityValidationException(Errors.CHANNEL_JSON_MISSING)
        }

        val effects = getEffectsFromJson(channelJson)
        validateChannelEffects(effects)
    }

    private fun getEffectsFromJson(rawAnimationData: String): SequenceChannelEffects {
        val animationData: SequenceChannelEffects
        try {
            animationData = GsonProvider.get().fromJson(rawAnimationData, SequenceChannelEffects::class.java)
        } catch (e: JsonSyntaxException) {
            throw EntityValidationException(Errors.CHANNEL_JSON_MALFORMED, e)
        }

        return animationData
    }

    private fun validateChannelEffects(channelEffects: SequenceChannelEffects) {
        var previousEndFrame: Int = -1

        for (effect in channelEffects.effects) {
            validateEffect(effect, previousEndFrame)
            previousEndFrame = effect.endFrame
        }
    }

    private fun validateEffect(effect: Effect, previousEndFrame: Int) {
        if (effect.type === EffectTypeCode.NONE) {
            throw EntityValidationException(String.format(Errors.EFFECT_TYPE_MISSING, effect.startFrame))
        }

        if (effect.easing.type === ParamType.NONE) {
            throw EntityValidationException(String.format(Errors.EFFECT_EASING_TYPE_MISSING, effect.startFrame))
        }

        if (effect.startFrame > effect.endFrame) {
            throw EntityValidationException(String.format(Errors.EFFECT_BACK_TO_FRONT, effect.startFrame))
        }

        if (effect.startFrame < previousEndFrame) {
            throw EntityValidationException(String.format(Errors.EFFECT_OVERLAPPING, previousEndFrame))
        }

        if (effect.repetitions <= 0) {
            throw EntityValidationException(String.format(Errors.EFFECT_REPETITIONS_INVALID, effect.startFrame))
        }

        val args = effect.getArguments()
        for (arg in args) {
            validateEffectArgument(effect, arg)
        }
    }

    private fun validateEffectArgument(effect: Effect, argument: Argument) {
        if (argument.code === ParamType.NONE) {
            throw EntityValidationException(String.format(Errors.EFFECT_ARGUMENT_CODE_MISSING, effect.startFrame))
        }
    }

    private object Errors {
        internal const val UUID_MISSING = "Sequence channel has no unique identifier."
        internal const val CHANNEL_JSON_MISSING = "Sequence has no animation data."
        internal const val CHANNEL_JSON_MALFORMED = "Sequence channel data is malformed."

        internal const val EFFECT_TYPE_MISSING = "Effect type cannot be empty for effect at frame %d in channel."
        internal const val EFFECT_EASING_TYPE_MISSING =
            "EasingFunction type cannot be empty for effect at frame %d in channel."
        internal const val EFFECT_BACK_TO_FRONT =
            "Effect start frame cannot be after end frame for effect at frame %d in channel."
        internal const val EFFECT_OVERLAPPING = "Overlapping or out-of-order effects detected at frame %d for channel."
        internal const val EFFECT_REPETITIONS_INVALID =
            "Effect repetitions cannot be less than 1 for effect at frame %d in channel."
        internal const val EFFECT_ARGUMENT_CODE_MISSING =
            "Effect argument code cannot be empty for effect at frame %d in channel."
    }
}
