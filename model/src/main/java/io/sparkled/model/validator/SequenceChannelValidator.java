package io.sparkled.model.validator;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.sparkled.model.animation.SequenceChannelEffects;
import io.sparkled.model.animation.effect.Effect;
import io.sparkled.model.animation.param.Param;
import io.sparkled.model.entity.SequenceChannel;
import io.sparkled.model.validator.exception.EntityValidationException;

import java.util.List;

public class SequenceChannelValidator {

    private static final Gson gson = new Gson();

    public void validate(SequenceChannel channel) throws EntityValidationException {
        String channelJson = channel.getChannelJson();

        if (channel.getUuid() == null) {
            throw new EntityValidationException(Errors.NO_UUID);
        } else if (channelJson == null) {
            throw new EntityValidationException(Errors.NO_CHANNEL_JSON);
        }

        SequenceChannelEffects effects = getEffectsFromJson(channelJson);
        validateChannelEffects(effects);
    }

    private SequenceChannelEffects getEffectsFromJson(String rawAnimationData) {
        SequenceChannelEffects animationData;
        try {
            animationData = gson.fromJson(rawAnimationData, SequenceChannelEffects.class);
        } catch (JsonSyntaxException e) {
            throw new EntityValidationException(Errors.MALFORMED_CHANNEL_JSON, e);
        }

        return animationData;
    }

    private void validateChannelEffects(SequenceChannelEffects channelEffects) {
        int previousEndFrame = -1;

        List<Effect> effects = channelEffects.getEffects();
        if (effects == null) {
            throw new EntityValidationException(Errors.NO_EFFECTS);
        }

        for (Effect effect : effects) {
            previousEndFrame = validateEffect(effect, previousEndFrame);
        }
    }

    private int validateEffect(Effect effect, int previousEndFrame) {
        int effectDuration = effect.getEndFrame() - effect.getStartFrame() + 1;

        if (effect.getType() == null) {
            throw new EntityValidationException(String.format(Errors.EFFECT_NO_TYPE, effect.getStartFrame()));
        }

        if (effect.getEasing() == null) {
            throw new EntityValidationException(String.format(Errors.EFFECT_EASING_NO_TYPE, effect.getStartFrame()));
        }

        if (effect.getStartFrame() > effect.getEndFrame()) {
            throw new EntityValidationException(String.format(Errors.EFFECT_BACK_TO_FRONT, effect.getStartFrame()));
        }

        if (effect.getStartFrame() < previousEndFrame) {
            throw new EntityValidationException(String.format(Errors.EFFECT_OVERLAPPING, previousEndFrame));
        }

        if (effect.getRepetitions() <= 0) {
            throw new EntityValidationException(String.format(Errors.EFFECT_INVALID_REPETITIONS, effect.getStartFrame()));
        }

        if (effect.getRepetitions() > effectDuration) {
            throw new EntityValidationException(String.format(Errors.EFFECT_TOO_MANY_REPETITIONS, effect.getStartFrame()));
        }

        if (effectDuration % effect.getRepetitions() > 0) {
            throw new EntityValidationException(String.format(Errors.EFFECT_INDIVISIBLE_DURATION, effect.getStartFrame()));
        }

        List<Param> params = effect.getParams();
        if (params == null) {
            throw new EntityValidationException(String.format(Errors.EFFECT_NO_PARAMS, effect.getStartFrame()));
        }

        for (Param param : params) {
            validateEffectParam(effect, param);
        }

        previousEndFrame = effect.getEndFrame();
        return previousEndFrame;
    }

    private void validateEffectParam(Effect effect, Param param) {
        if (param.getType() == null) {
            throw new EntityValidationException(String.format(Errors.EFFECT_PARAM_NO_TYPE, effect.getStartFrame()));
        }
    }

    private static class Errors {
        static final String NO_UUID = "Sequence channel has no unique identifier.";
        static final String NO_CHANNEL_JSON = "Sequence has no animation data.";
        static final String MALFORMED_CHANNEL_JSON = "Sequence channel data is malformed.";
        static final String NO_EFFECTS = "Effects list must be populated for sequence effect channel.";

        static final String EFFECT_NO_TYPE = "Effect type cannot be empty for effect at frame %d in channel.";
        static final String EFFECT_EASING_NO_TYPE = "EasingFunction type cannot be empty for effect at frame %d in channel.";
        static final String EFFECT_BACK_TO_FRONT = "Effect start frame cannot be after end frame for effect at frame %d in channel.";
        static final String EFFECT_OVERLAPPING = "Overlapping or out-of-order effects detected at frame %d for channel.";
        static final String EFFECT_INVALID_REPETITIONS = "Effect repetitions cannot be less than 1 for effect at frame %d in channel.";
        static final String EFFECT_TOO_MANY_REPETITIONS = "Effect repetitions cannot be greater than the frame count at frame %d in channel.";
        static final String EFFECT_INDIVISIBLE_DURATION = "Duration must be evenly divisible by number of repetitions for effect at frame %d in channel.";
        static final String EFFECT_NO_PARAMS = "Effect parameters list is not populated for effect at frame %d in channel.";
        static final String EFFECT_PARAM_NO_TYPE = "Effect parameter type cannot be empty for effect at frame %d in channel.";
    }
}
