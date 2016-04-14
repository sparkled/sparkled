package net.chrisparton.xmas.preprocessor;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.chrisparton.xmas.entity.*;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SongPreprocessor {

    private static final Gson gson = new Gson();
    private Song song;
    private SongAnimationData animationData;

    public SongPreprocessor(Song song) {
        this.song = song;
    }

    public void validate() throws EntityValidationException {
        String rawAnimationData = song.getAnimationData();

        if (rawAnimationData == null) {
            throw new EntityValidationException("Song has no animation data.");
        }

        animationData = parseAnimationData(rawAnimationData);

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

        if (StringUtils.isEmpty(channelName)) {
            throw new EntityValidationException("Animation effect channel doesn't have a name.");
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
            throw new EntityValidationException("Channel effects list cannot be null.");
        }

        for (AnimationEffect effect : effects) {
            if (effect.getStartFrame() > effect.getEndFrame()) {
                throw new EntityValidationException("Effect start frame cannot be after end frame.");
            } else if (effect.getStartFrame() < previousEndFrame) {
                throw new EntityValidationException("Overlapping or out-of-order effects detected at frame " + previousEndFrame + " for channel " + channelName + '.');
            }

            List<AnimationEffectParam> params = effect.getParams();
            if (params == null) {
                throw new EntityValidationException("Effect params list cannot be null.");
            }

            for (AnimationEffectParam param : params) {
                if (StringUtils.isEmpty(param.getParamCode())) {
                    throw new EntityValidationException("Effect param type cannot be null.");
                }
            }

            previousEndFrame = effect.getEndFrame();
        }
    }

    public void escapeText() {
        if (animationData == null) {
            throw new EntityValidationException("Song has not been validated yet.");
        }

        escape(song::getName, song::setName);
        escape(song::getAlbum, song::setAlbum);
        escape(song::getArtist, song::setArtist);

        animationData.getChannels().forEach(channel -> {
            escape(channel::getName, channel::setName);

            List<AnimationEffect> effects = channel.getEffects();
            effects.forEach(effect -> {
                effect.getParams().forEach(param -> {
                    escape(param::getValue, param::setValue);
                });
            });
        });
    }

    private void escape(Supplier<String> getter, Consumer<String> setter) {
        String value = getter.get();
        String escapedValue = StringEscapeUtils.escapeHtml4(value);

        setter.accept(escapedValue);
    }
}
