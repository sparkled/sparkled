package io.sparkled.udpserver.impl.command

import io.sparkled.model.render.RenderedFrame
import io.sparkled.model.setting.SettingsCache
import io.sparkled.model.util.SequenceUtils
import io.sparkled.music.PlaybackState

/**
 * Retrieves the rendered stage prop data frame for the current sequence, synchronised to the current point of playback.
 * Returns an error response if no rendered frame is found for the stage prop.
 * Command syntax: GF:StagePropCode, e.g. GF:P1
 */
class GetFrameCommand : RequestCommand() {

    override fun getResponse(args: List<String>, settings: SettingsCache, playbackState: PlaybackState): ByteArray {
        if (args.size != 2 || playbackState.isEmpty) {
            return errorResponse
        }

        val stagePropCode = args[1]
        val frameCount = SequenceUtils.getFrameCount(playbackState.song!!, playbackState.sequence!!)

        val renderedFrame = getRenderedFrame(playbackState, stagePropCode, Math.min(frameCount - 1, Math.round(playbackState.progress * frameCount).toInt()))
        return buildFrame(renderedFrame, settings)
    }

    private fun buildFrame(renderedFrame: RenderedFrame?, settings: SettingsCache): ByteArray {
        val brightness = settings.brightness
        val headerData = buildHeader(brightness)
        val frameData = renderedFrame?.getData() ?: ByteArray(0)

        val headerAndData = ByteArray(headerData.size + frameData.size)
        System.arraycopy(headerData, 0, headerAndData, 0, headerData.size)
        System.arraycopy(frameData, 0, headerAndData, headerData.size, frameData.size)

        return headerAndData
    }

    private fun buildHeader(brightness: Int): ByteArray {
        return byteArrayOf((brightness shl 4).toByte()) // BBBB0000
    }

    private fun getRenderedFrame(playbackState: PlaybackState, stagePropCode: String, frameIndex: Int): RenderedFrame? {
        val renderedStageProps = playbackState.renderedStageProps
        val stagePropUuid = playbackState.stagePropUuids[stagePropCode]

        val renderedStagePropData = renderedStageProps!![stagePropUuid]
        val frames = renderedStagePropData?.frames ?: ArrayList()
        return if (frameIndex >= frames.size) null else frames[frameIndex]
    }

    companion object {
        const val KEY = "GF"
    }
}
