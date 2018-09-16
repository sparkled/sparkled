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
            throw new EntityValidationException("Sequence channel has no unique identifier.");
        } else if (channelJson == null) {
            throw new EntityValidationException("Sequence has no animation data.");
        }

        SequenceChannelEffects effects = getEffectsFromJson(channelJson);
        validateChannelEffects(effects);
    }

    private SequenceChannelEffects getEffectsFromJson(String rawAnimationData) {
        SequenceChannelEffects animationData;
        try {
            animationData = gson.fromJson(rawAnimationData, SequenceChannelEffects.class);
        } catch (JsonSyntaxException e) {
            throw new EntityValidationException("Sequence channel data is malformed.", e);
        }

        return animationData;
    }

    private void validateChannelEffects(SequenceChannelEffects channelEffects) {
        int previousEndFrame = -1;

        List<Effect> effects = channelEffects.getEffects();
        if (effects == null) {
            String errorMessage = "Effects list must be populated for sequence effect channel.";
            throw new EntityValidationException(errorMessage);
        }

        for (Effect effect : effects) {
            int effectDuration = effect.getEndFrame() - effect.getStartFrame();

            if (effect.getType() == null) {
                String errorMessage = String.format(
                        "Effect type cannot be empty for effect at frame %d in channel.",
                        effect.getStartFrame()
                );
                throw new EntityValidationException(errorMessage);
            }

            if (effect.getEasing() == null) {
                String errorMessage = String.format(
                        "EasingFunction type cannot be empty for effect at frame %d in channel.",
                        effect.getStartFrame()
                );
                throw new EntityValidationException(errorMessage);
            }

            if (effect.getStartFrame() > effect.getEndFrame()) {
                String errorMessage = String.format(
                        "Effect start frame cannot be after end frame for effect at frame %d in channel.",
                        effect.getStartFrame()
                );
                throw new EntityValidationException(errorMessage);
            } else if (effect.getStartFrame() < previousEndFrame) {
                String errorMessage = String.format(
                        "Overlapping or out-of-order effects detected at frame %d for channel.",
                        previousEndFrame
                );
                throw new EntityValidationException(errorMessage);
            }

            if (effect.getRepetitions() <= 0) {
                String errorMessage = String.format(
                        "Effect repetitions cannot be less than 1 for effect at frame %d in channel.",
                        effect.getStartFrame()
                );
                throw new EntityValidationException(errorMessage);
            } else {
                if (effect.getRepetitions() > effectDuration) {
                    String errorMessage = String.format(
                            "Effect repetitions cannot be greater than the frame count at frame %d in channel.",
                            effect.getStartFrame()
                    );
                    throw new EntityValidationException(errorMessage);
                } else if (effectDuration % effect.getRepetitions() > 0) {
                    String errorMessage = String.format(
                            "Duration must be evenly divisible by number of repetitions for effect at frame %d in channel.",
                            effect.getStartFrame()
                    );
                    throw new EntityValidationException(errorMessage);
                }
            }

            List<Param> params = effect.getParams();
            if (params == null) {
                String errorMessage = String.format(
                        "Effect parameters list is not populated for effect at frame %d in channel.",
                        effect.getStartFrame()
                );
                throw new EntityValidationException(errorMessage);
            }

            for (Param param : params) {
                if (param.getType() == null) {
                    String errorMessage = String.format(
                            "Effect parameter type cannot be empty for effect at frame %d in channel.",
                            effect.getStartFrame()
                    );
                    throw new EntityValidationException(errorMessage);
                }
            }

            previousEndFrame = effect.getEndFrame();
        }
    }
}
