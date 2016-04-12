package net.chrisparton.xmas.preprocessor;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import net.chrisparton.xmas.entity.AnimationEffect;
import net.chrisparton.xmas.entity.AnimationEffectChannel;
import net.chrisparton.xmas.entity.Song;
import net.chrisparton.xmas.entity.SongAnimationData;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SongPreprocessor {

    private static final Gson gson = new Gson();
    private Song song;
    private SongAnimationData animationData;

    public SongPreprocessor(Song song) {
        this.song = song;
    }

    public void validate() throws IllegalStateException {
        String rawAnimationData = song.getAnimationData();

        if (rawAnimationData == null) {
            throw new IllegalStateException("Song has no animation data.");
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
            throw new IllegalStateException("Animation data is malformed.");
        }

        return animationData;
    }

    private void validateChannel(AnimationEffectChannel animationEffectChannel) {
        String channelName = animationEffectChannel.getName();
        int startLed = animationEffectChannel.getStartLed();
        int endLed = animationEffectChannel.getEndLed();

        if (channelName == null) {
            throw new IllegalStateException("Animation effect channel doesn't have a name.");
        }

        if (startLed < 0) {
            throw new IllegalStateException("Start LED cannot be negative for channel " + channelName + '.');
        } else if (startLed > endLed) {
            throw new IllegalStateException("Start LED cannot be after end LED for channel " + channelName + '.');
        }
    }

    private void validateChannelEffects(AnimationEffectChannel animationEffectChannel) {
        int previousStartFrame = -1;
        int previousEndFrame = -1;
        String channelName = animationEffectChannel.getName();

        for (AnimationEffect effect : animationEffectChannel.getEffects()) {
            if (effect.getStartFrame() < previousEndFrame) {
                throw new IllegalStateException(getOverlapErrorMessage(channelName, previousEndFrame));
            } else if (effect.getEndFrame() > previousStartFrame) {
                throw new IllegalStateException(getOverlapErrorMessage(channelName, previousStartFrame));
            }

            previousStartFrame = effect.getStartFrame();
            previousEndFrame = effect.getEndFrame();
        }
    }

    private String getOverlapErrorMessage(String channelName, int previousEndFrame) {
        return "Overlapping or out-of-order effects detected at frame " + previousEndFrame + " for channel " + channelName + '.';
    }

    public void escapeText() {
        if (animationData == null) {
            throw new IllegalStateException("Song has not been validated yet.");
        }

        escape(song::getName, song::setName);
        escape(song::getAlbum, song::setAlbum);
        escape(song::getArtist, song::setArtist);

        animationData.getChannels().forEach(channel -> {
            escape(channel::getName, channel::setName);

            channel.getEffects().forEach(effect -> {
                effect.getParams().entrySet().forEach(paramMap -> {
                    escape(paramMap::getValue, paramMap::setValue);
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
