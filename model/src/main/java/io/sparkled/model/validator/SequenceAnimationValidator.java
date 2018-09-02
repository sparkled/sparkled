package io.sparkled.model.validator;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import io.sparkled.model.animation.SequenceAnimationData;
import io.sparkled.model.animation.effect.Effect;
import io.sparkled.model.animation.effect.EffectChannel;
import io.sparkled.model.animation.param.Param;
import io.sparkled.model.entity.SequenceAnimation;
import io.sparkled.model.validator.exception.EntityValidationException;

import java.util.List;

public class SequenceAnimationValidator {

    private static final Gson gson = new Gson();

    public void validate(SequenceAnimation sequenceAnimation) throws EntityValidationException {
        String rawAnimationData = sequenceAnimation.getAnimationData();

        if (rawAnimationData == null) {
            throw new EntityValidationException("Sequence has no animation data.");
        }

        SequenceAnimationData animationData = parseAnimationData(rawAnimationData);

        animationData.getChannels().forEach(channel -> {
            validateChannel(channel);
            validateChannelEffects(channel);
        });
    }

    private SequenceAnimationData parseAnimationData(String rawAnimationData) {
        SequenceAnimationData animationData;
        try {
            animationData = gson.fromJson(rawAnimationData, SequenceAnimationData.class);
        } catch (JsonSyntaxException e) {
            throw new EntityValidationException("Animation data is malformed.", e);
        }

        return animationData;
    }

    private void validateChannel(EffectChannel effectChannel) {
        String channelName = effectChannel.getName();
        int startLed = effectChannel.getStartLed();
        int endLed = effectChannel.getEndLed();

        if (channelName == null || channelName.isEmpty()) {
            throw new EntityValidationException("Animation effect channels must have names.");
        }

        if (startLed < 0) {
            throw new EntityValidationException("Start LED cannot be negative for channel " + channelName + '.');
        } else if (startLed > endLed) {
            throw new EntityValidationException("Start LED cannot be after end LED for channel " + channelName + '.');
        }
    }

    private void validateChannelEffects(EffectChannel channel) {
        int previousEndFrame = -1;
        String channelName = channel.getName();

        List<Effect> effects = channel.getEffects();
        if (effects == null) {
            String errorMessage = "Effects list must be populated for channel '" + channelName + "'.";
            throw new EntityValidationException(errorMessage);
        }

        for (Effect effect : effects) {
            int effectDuration = effect.getEndFrame() - effect.getStartFrame();

            if (effect.getType() == null) {
                String errorMessage = String.format(
                        "Effect type cannot be empty for effect at frame %d in channel '%s'.",
                        effect.getStartFrame(), channelName
                );
                throw new EntityValidationException(errorMessage);
            }

            if (effect.getEasing() == null) {
                String errorMessage = String.format(
                        "EasingFunction type cannot be empty for effect at frame %d in channel '%s'.",
                        effect.getStartFrame(), channelName
                );
                throw new EntityValidationException(errorMessage);
            }

            if (effect.getStartFrame() > effect.getEndFrame()) {
                String errorMessage = String.format(
                        "Effect start frame cannot be after end frame for effect at frame %d in channel '%s'.",
                        effect.getStartFrame(), channelName
                );
                throw new EntityValidationException(errorMessage);
            } else if (effect.getStartFrame() < previousEndFrame) {
                String errorMessage = String.format(
                        "Overlapping or out-of-order effects detected at frame %d for channel '%s'.",
                        previousEndFrame,
                        channelName
                );
                throw new EntityValidationException(errorMessage);
            }

            if (effect.getRepetitions() <= 0) {
                String errorMessage = String.format(
                        "Effect repetitions cannot be less than 1 for effect at frame %d in channel '%s'.",
                        effect.getStartFrame(), channelName
                );
                throw new EntityValidationException(errorMessage);
            } else {
                if (effect.getRepetitions() > effectDuration) {
                    String errorMessage = String.format(
                            "Effect repetitions cannot be greater than the frame count at frame %d in channel '%s'.",
                            effect.getStartFrame(), channelName
                    );
                    throw new EntityValidationException(errorMessage);
                } else if (effectDuration % effect.getRepetitions() > 0) {
                    String errorMessage = String.format(
                            "Duration must be evenly divisible by number of repetitions for effect at frame %d in channel '%s'.",
                            effect.getStartFrame(), channelName
                    );
                    throw new EntityValidationException(errorMessage);
                }
            }

            List<Param> params = effect.getParams();
            if (params == null) {
                String errorMessage = String.format(
                        "Effect parameters list is not populated for effect at frame %d in channel '%s'.",
                        effect.getStartFrame(), channelName
                );
                throw new EntityValidationException(errorMessage);
            }

            for (Param param : params) {
                if (param.getType() == null) {
                    String errorMessage = String.format(
                            "Effect parameter type cannot be empty for effect at frame %d in channel '%s'.",
                            effect.getStartFrame(), channelName
                    );
                    throw new EntityValidationException(errorMessage);
                }
            }

            previousEndFrame = effect.getEndFrame();
        }
    }
}
