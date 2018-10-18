package io.sparkled.udpserver.impl.command;

import io.sparkled.model.render.RenderedFrame;
import io.sparkled.model.render.RenderedStagePropData;
import io.sparkled.model.render.RenderedStagePropDataMap;
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
    public byte[] getResponse(String[] args, PlaybackState playbackState) {
        if (args.length != 2 || playbackState.isEmpty()) {
            return getErrorResponse();
        }

        String stagePropCode = args[1];

        final int frameCount = SequenceUtils.getFrameCount(playbackState.getSong(), playbackState.getSequence());
        final int frameIndex = (int) Math.min(frameCount - 1, Math.round(playbackState.getProgress() * frameCount));

        RenderedFrame renderedFrame = getRenderedFrame(playbackState, stagePropCode, frameIndex);
        return renderedFrame == null ? getErrorResponse() : renderedFrame.getData();
    }

    private RenderedFrame getRenderedFrame(PlaybackState playbackState, String stagePropCode, int frameIndex) {
        RenderedStagePropDataMap renderedStageProps = playbackState.getRenderedStageProps();
        UUID stagePropUuid = playbackState.getStagePropUuids().get(stagePropCode);

        RenderedStagePropData renderedStagePropData = renderedStageProps.getOrDefault(stagePropUuid, RenderedStagePropData.EMPTY);
        List<RenderedFrame> frames = renderedStagePropData.getFrames();
        return frameIndex >= frames.size() ? null : frames.get(frameIndex);
    }
}
