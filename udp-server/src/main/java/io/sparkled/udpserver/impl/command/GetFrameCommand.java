package io.sparkled.udpserver.impl.command;

import io.sparkled.model.render.RenderedFrame;
import io.sparkled.model.render.RenderedStagePropData;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.model.setting.SettingsCache;
import io.sparkled.model.util.SequenceUtils;
import io.sparkled.music.PlaybackState;

import java.util.List;
import java.util.UUID;

/**
 * Retrieves the rendered stage prop data frame for the current sequence, synchronised to the current point of playback.
 * Returns an error response if no rendered frame is found for the stage prop.
 * Command syntax: GF:<StagePropCode>, e.g. GF:P1
 */
public class GetFrameCommand extends RequestCommand {

    public static final String KEY = "GF";

    @Override
    public byte[] getResponse(String[] args, SettingsCache settings, PlaybackState playbackState) {
        if (args.length != 2 || playbackState.isEmpty()) {
            return getErrorResponse();
        }

        String stagePropCode = args[1];
        final int frameCount = SequenceUtils.getFrameCount(playbackState.getSong(), playbackState.getSequence());
        final int frameIndex = (int) Math.min(frameCount - 1, Math.round(playbackState.getProgress() * frameCount));

        RenderedFrame renderedFrame = getRenderedFrame(playbackState, stagePropCode, frameIndex);
        return buildFrame(renderedFrame, settings);
    }

    private byte[] buildFrame(RenderedFrame renderedFrame, SettingsCache settings) {
        int brightness = settings.getBrightness();
        byte[] headerData = buildHeader(brightness);
        byte[] frameData = renderedFrame == null ? new byte[0] : renderedFrame.getData();

        byte[] headerAndData = new byte[headerData.length + frameData.length];
        System.arraycopy(headerData, 0, headerAndData, 0, headerData.length);
        System.arraycopy(frameData, 0, headerAndData, headerData.length, frameData.length);

        return headerAndData;
    }

    private byte[] buildHeader(int brightness) {
        return new byte[]{(byte) (brightness << 4)}; // BBBB0000
    }

    private RenderedFrame getRenderedFrame(PlaybackState playbackState, String stagePropCode, int frameIndex) {
        RenderedStagePropDataMap renderedStageProps = playbackState.getRenderedStageProps();
        UUID stagePropUuid = playbackState.getStagePropUuids().get(stagePropCode);

        RenderedStagePropData renderedStagePropData = renderedStageProps.getOrDefault(stagePropUuid, RenderedStagePropData.EMPTY);
        List<RenderedFrame> frames = renderedStagePropData.getFrames();
        return frameIndex >= frames.size() ? null : frames.get(frameIndex);
    }
}
