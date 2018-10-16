package io.sparkled.music.impl;

import io.sparkled.music.MusicPlayerService;
import io.sparkled.music.PlaybackState;
import javazoom.jl.player.advanced.PlaybackEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class MusicPlayerServiceImpl implements MusicPlayerService, LineListener {

    private static final Logger logger = LoggerFactory.getLogger(MusicPlayerServiceImpl.class);

    private final Set<LineListener> listeners = new HashSet<>();
    private Clip clip;

    @Inject
    public MusicPlayerServiceImpl() {
    }

    @Override
    public void play(PlaybackState playbackState) {
        stopPlayback();

        InputStream byteStream = null;
        AudioInputStream mp3Stream = null;
        AudioInputStream convertedStream = null;

        try {
            logger.info("Playing sequence {}.", playbackState.getSequence().getName());

            byteStream = new ByteArrayInputStream(playbackState.getSongAudio().getAudioData());
            mp3Stream = AudioSystem.getAudioInputStream(byteStream);

            AudioFormat baseFormat = mp3Stream.getFormat();
            AudioFormat decodedFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, baseFormat.getSampleRate(), 16, baseFormat.getChannels(),
                    baseFormat.getChannels() * 2, baseFormat.getSampleRate(), false);
            convertedStream = AudioSystem.getAudioInputStream(decodedFormat, mp3Stream);

            clip = AudioSystem.getClip();
            clip.open(convertedStream);
            clip.addLineListener(this);
            clip.start();
            logger.info("Sequence finished playing.");
        } catch (Exception e) {
            logger.error("Failed to play sequence {}: {}.", playbackState.getSequence().getName(), e.getMessage());
        } finally {
            close(byteStream, mp3Stream, convertedStream);
        }
    }

    private void close(InputStream... streams) {
        for (int i = 0; i < streams.length; i++) {
            InputStream stream = streams[i];
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    logger.error("Failed to close stream #{}: {}.", i, e.getMessage());
                }
            }
        }
    }

    public void addLineListener(LineListener listener) {
        this.listeners.add(listener);
        logger.info("Added line listener: {}.", listener);
    }

    @Override
    public double getSequenceProgress() {
        if (clip == null) {
            return 0d;
        } else {
            return Math.min(1f, clip.getFramePosition() / (double) clip.getFrameLength());
        }
    }

    @Override
    public void stopPlayback() {
        if (clip != null && clip.isOpen()) {
            try {
                clip.close();
                logger.info("Clip closed.");
            } catch (Exception e) {
                logger.error("Failed to close clip.");
            }
        } else {
            logger.info("Clip is already closed.");
        }

        clip = null;
    }

    @Override
    public void update(LineEvent event) {
        listeners.forEach(l -> l.update(event));
    }
}
