package io.sparkled.udpserver.impl.command

import io.sparkled.model.render.RenderedFrame
import io.sparkled.model.setting.SettingsCache
import io.sparkled.model.util.SequenceUtils
import io.sparkled.music.PlaybackState

/**
 * Retrieves the rendered stage prop data frame for the current sequence, synchronised to the current point of playback.
 * Returns an empty response if no rendered frame is found for the stage prop.
 * Command syntax: GF:StagePropCode[:ClientID], e.g. GF:P1:1 or GF:P1 (ClientID defaults to 0).
 */
class GetFrameCommand : RequestCommand() {

    override fun getResponse(args: List<String>, settings: SettingsCache, playbackState: PlaybackState): ByteArray {
        val frameData = when {
            args.size < 2 -> blackFrame
            playbackState.isEmpty -> blackFrame
            else -> {
                val stagePropCode = args[1]
                val frameCount = SequenceUtils.getFrameCount(playbackState.song!!, playbackState.sequence!!)

                val frameIndex = Math.min(frameCount - 1, Math.round(playbackState.progress * frameCount).toInt())
                val renderedFrame = getRenderedFrame(playbackState, stagePropCode, frameIndex)
                renderedFrame?.getData() ?: blackFrame
            }
        }

        val clientId = (args.getOrNull(2) ?: "0").toInt()
        return buildFrame(frameData, settings, clientId)
    }

    private fun buildFrame(frameData: ByteArray, settings: SettingsCache, clientId: Int): ByteArray {
        val brightness = settings.brightness
        val headerData = buildHeader(clientId, brightness)

        val headerAndData = ByteArray(headerData.size + frameData.size)
        System.arraycopy(headerData, 0, headerAndData, 0, headerData.size)
        System.arraycopy(frameData, 0, headerAndData, headerData.size, frameData.size)

        return headerAndData
    }

    private fun buildHeader(clientId: Int, brightness: Int): ByteArray {
        val headerClientId = clientId shl 4 and 0b11110000 // CCCC0000
        val headerBrightness = brightness and 0b00001111 // 0000BBBB
        return byteArrayOf((headerClientId or headerBrightness).toByte()) // CCCCBBBB
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
        private val blackFrame = byteArrayOf(0, 0, 0)
    }
}
