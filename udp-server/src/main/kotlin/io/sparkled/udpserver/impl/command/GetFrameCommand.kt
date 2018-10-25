package io.sparkled.udpserver.impl.command

import io.sparkled.model.render.RenderedFrame
import io.sparkled.model.render.RenderedStagePropData
import io.sparkled.model.render.RenderedStagePropDataMap
import io.sparkled.model.setting.SettingsCache
import io.sparkled.model.util.SequenceUtils
import io.sparkled.music.PlaybackState
import java.util.UUID

/**
 * Retrieves the rendered stage prop data frame for the current sequence, synchronised to the current point of playback.
 * Returns an error response if no rendered frame is found for the stage prop.
 * Command syntax: GF:<StagePropCode>, e.g. GF:P1
</StagePropCode> */
class GetFrameCommand : RequestCommand() {

    @Override
    fun getResponse(args: Array<String>, settings: SettingsCache, playbackState: PlaybackState): ByteArray {
        if (args.size != 2 || playbackState.isEmpty()) {
            return getErrorResponse()
        }

        val stagePropCode = args[1]
        val frameCount = SequenceUtils.getFrameCount(playbackState.getSong(), playbackState.getSequence())

        val renderedFrame = getRenderedFrame(playbackState, stagePropCode, Math.min(frameCount - 1, Math.round(playbackState.getProgress() * frameCount)))
        return buildFrame(renderedFrame, settings)
    }

    private fun buildFrame(renderedFrame: RenderedFrame?, settings: SettingsCache): ByteArray {
        val brightness = settings.getBrightness()
        val headerData = buildHeader(brightness)
        val frameData = if (renderedFrame == null) ByteArray(0) else renderedFrame!!.getData()

        val headerAndData = ByteArray(headerData.size + frameData.size)
        System.arraycopy(headerData, 0, headerAndData, 0, headerData.size)
        System.arraycopy(frameData, 0, headerAndData, headerData.size, frameData.size)

        return headerAndData
    }

    private fun buildHeader(brightness: Int): ByteArray {
        return byteArrayOf((brightness shl 4).toByte()) // BBBB0000
    }

    private fun getRenderedFrame(playbackState: PlaybackState, stagePropCode: String, frameIndex: Int): RenderedFrame? {
        val renderedStageProps = playbackState.getRenderedStageProps()
        val stagePropUuid = playbackState.getStagePropUuids().get(stagePropCode)

        val renderedStagePropData = renderedStageProps.getOrDefault(stagePropUuid, RenderedStagePropData.EMPTY)
        val frames = renderedStagePropData.getFrames()
        return if (frameIndex >= frames.size()) null else frames.get(frameIndex)
    }

    companion object {

        val KEY = "GF"
    }
}
