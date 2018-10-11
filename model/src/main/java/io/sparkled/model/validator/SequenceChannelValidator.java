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
            throw new EntityValidationException(Errors.UUID_MISSING);
        } else if (channelJson == null) {
            throw new EntityValidationException(Errors.CHANNEL_JSON_MISSING);
        }

        SequenceChannelEffects effects = getEffectsFromJson(channelJson);
        validateChannelEffects(effects);
    }

    private SequenceChannelEffects getEffectsFromJson(String rawAnimationData) {
        SequenceChannelEffects animationData;
        try {
            animationData = gson.fromJson(rawAnimationData, SequenceChannelEffects.class);
        } catch (JsonSyntaxException e) {
            throw new EntityValidationException(Errors.CHANNEL_JSON_MALFORMED, e);
        }

        return animationData;
    }

    private void validateChannelEffects(SequenceChannelEffects channelEffects) {
        int previousEndFrame = -1;

        List<Effect> effects = channelEffects.getEffects();
        if (effects == null) {
            throw new EntityValidationException(Errors.EFFECTS_MISSING);
        }

        for (Effect effect : effects) {
            previousEndFrame = validateEffect(effect, previousEndFrame);
        }
    }

    private int validateEffect(Effect effect, int previousEndFrame) {
        int effectDuration = effect.getEndFrame() - effect.getStartFrame() + 1;

        if (effect.getType() == null) {
            throw new EntityValidationException(String.format(Errors.EFFECT_TYPE_MISSING, effect.getStartFrame()));
        }

        if (effect.getEasing() == null) {
            throw new EntityValidationException(String.format(Errors.EFFECT_EASING_TYPE_MISSING, effect.getStartFrame()));
        }

        if (effect.getStartFrame() > effect.getEndFrame()) {
            throw new EntityValidationException(String.format(Errors.EFFECT_BACK_TO_FRONT, effect.getStartFrame()));
        }

        if (effect.getStartFrame() < previousEndFrame) {
            throw new EntityValidationException(String.format(Errors.EFFECT_OVERLAPPING, previousEndFrame));
        }

        if (effect.getRepetitions() <= 0) {
            throw new EntityValidationException(String.format(Errors.EFFECT_REPETITIONS_INVALID, effect.getStartFrame()));
        }

        if (effect.getRepetitions() > effectDuration) {
            throw new EntityValidationException(String.format(Errors.EFFECT_REPETITIONS_TOO_MANY, effect.getStartFrame()));
        }

        if (effectDuration % effect.getRepetitions() > 0) {
            throw new EntityValidationException(String.format(Errors.EFFECT_DURATION_INDIVISIBLE, effect.getStartFrame()));
        }

        List<Param> params = effect.getParams();
        if (params == null) {
            throw new EntityValidationException(String.format(Errors.EFFECT_PARAMS_MISSING, effect.getStartFrame()));
        }

        for (Param param : params) {
            validateEffectParam(effect, param);
        }

        previousEndFrame = effect.getEndFrame();
        return previousEndFrame;
    }

    private void validateEffectParam(Effect effect, Param param) {
        if (param.getType() == null) {
            throw new EntityValidationException(String.format(Errors.EFFECT_PARAM_TYPE_MISSING, effect.getStartFrame()));
        }
    }

    private static class Errors {
        static final String UUID_MISSING = "Sequence channel has no unique identifier.";
        static final String CHANNEL_JSON_MISSING = "Sequence has no animation data.";
        static final String CHANNEL_JSON_MALFORMED = "Sequence channel data is malformed.";
        static final String EFFECTS_MISSING = "Effects list must be populated for sequence effect channel.";

        static final String EFFECT_TYPE_MISSING = "Effect type cannot be empty for effect at frame %d in channel.";
        static final String EFFECT_EASING_TYPE_MISSING = "EasingFunction type cannot be empty for effect at frame %d in channel.";
        static final String EFFECT_BACK_TO_FRONT = "Effect start frame cannot be after end frame for effect at frame %d in channel.";
        static final String EFFECT_OVERLAPPING = "Overlapping or out-of-order effects detected at frame %d for channel.";
        static final String EFFECT_REPETITIONS_INVALID = "Effect repetitions cannot be less than 1 for effect at frame %d in channel.";
        static final String EFFECT_REPETITIONS_TOO_MANY = "Effect repetitions cannot be greater than the frame count at frame %d in channel.";
        static final String EFFECT_DURATION_INDIVISIBLE = "Duration must be evenly divisible by number of repetitions for effect at frame %d in channel.";
        static final String EFFECT_PARAMS_MISSING = "Effect parameters list is not populated for effect at frame %d in channel.";
        static final String EFFECT_PARAM_TYPE_MISSING = "Effect parameter type cannot be empty for effect at frame %d in channel.";
    }
}
