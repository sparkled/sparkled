package net.chrisparton.sparkled.model.validator;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.chrisparton.sparkled.model.animation.AnimationEffect;
import net.chrisparton.sparkled.model.animation.AnimationEffectChannel;
import net.chrisparton.sparkled.model.animation.AnimationEffectParam;
import net.chrisparton.sparkled.model.animation.SongAnimationData;
import net.chrisparton.sparkled.model.entity.SongAnimation;
import net.chrisparton.sparkled.model.validator.exception.EntityValidationException;

import java.util.List;

public class SongAnimationValidator {

    private static final Gson gson = new Gson();
    private SongAnimation songAnimation;

    public SongAnimationValidator(SongAnimation songAnimation) {
        this.songAnimation = songAnimation;
    }

    public void validate() throws EntityValidationException {
        String rawAnimationData = songAnimation.getAnimationData();

        if (rawAnimationData == null) {
            throw new EntityValidationException("Song has no animation data.");
        }

        SongAnimationData animationData = parseAnimationData(rawAnimationData);

        animationData.getChannels().forEach(channel -> {
            validateChannel(channel);
            validateChannelEffects(channel);
        });
    }

    private SongAnimationData parseAnimationData(String rawAnimationData) {
        SongAnimationData animationData;
        try {
            animationData = gson.fromJson(rawAnimationData, SongAnimationData.class);
        } catch (JsonSyntaxException e) {
            throw new EntityValidationException("Animation data is malformed.");
        }

        return animationData;
    }

    private void validateChannel(AnimationEffectChannel animationEffectChannel) {
        String channelName = animationEffectChannel.getName();
        int startLed = animationEffectChannel.getStartLed();
        int endLed = animationEffectChannel.getEndLed();

        if (channelName == null || channelName.isEmpty()) {
            throw new EntityValidationException("Animation effect channels must have names.");
        }

        if (startLed < 0) {
            throw new EntityValidationException("Start LED cannot be negative for channel " + channelName + '.');
        } else if (startLed > endLed) {
            throw new EntityValidationException("Start LED cannot be after end LED for channel " + channelName + '.');
        }
    }

    private void validateChannelEffects(AnimationEffectChannel channel) {
        int previousEndFrame = -1;
        String channelName = channel.getName();

        List<AnimationEffect> effects = channel.getEffects();
        if (effects == null) {
            String errorMessage = "Effects list must be populated for channel '" + channelName + "'.";
            throw new EntityValidationException(errorMessage);
        }

        for (AnimationEffect effect : effects) {
            int effectDuration = effect.getEndFrame() - effect.getStartFrame();

            if (effect.getEffectType() == null) {
                String errorMessage = String.format(
                        "Effect type cannot be empty for effect at frame %d in channel '%s'.",
                        effect.getStartFrame(), channelName
                );
                throw new EntityValidationException(errorMessage);
            }

            if (effect.getEasingType() == null) {
                String errorMessage = String.format(
                        "Easing type cannot be empty for effect at frame %d in channel '%s'.",
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

            List<AnimationEffectParam> params = effect.getParams();
            if (params == null) {
                String errorMessage = String.format(
                        "Effect parameters list is not populated for effect at frame %d in channel '%s'.",
                        effect.getStartFrame(), channelName
                );
                throw new EntityValidationException(errorMessage);
            }

            for (AnimationEffectParam param : params) {
                if (param.getParamCode() == null) {
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
