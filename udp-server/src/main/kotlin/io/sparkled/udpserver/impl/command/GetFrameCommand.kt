package io.sparkled.udpserver.impl.command

import io.sparkled.model.render.RenderedFrame
import io.sparkled.model.setting.SettingsCache
import io.sparkled.model.util.SequenceUtils
import io.sparkled.music.PlaybackState
import java.net.InetAddress
import kotlin.math.min
import kotlin.math.round

/**
 * Retrieves the rendered stage prop data frame for the current sequence, synchronised to the current point of playback.
 * Returns an empty response if no rendered frame is found for the stage prop.
 * Command syntax: GF:StagePropCode[:ClientID], e.g. GF:P1:1 or GF:P1 (ClientID defaults to 0).
 */
class GetFrameCommand : UdpCommand {

    override fun handle(ipAddress: InetAddress, port: Int, args: List<String>, settings: SettingsCache, playbackState: PlaybackState): ByteArray {
        val stagePropCode = args[1]
        val clientId = args[2].toInt()
        val brightness = calculateBrightness(stagePropCode, settings, playbackState)

        val headerData = buildHeader(clientId, brightness)
        val frameData = buildFrame(stagePropCode, playbackState)
        return buildResponse(headerData, frameData)
    }

    private fun calculateBrightness(stagePropCode: String, settings: SettingsCache, playbackState: PlaybackState): Int {
        val propBrightness = (playbackState.stageProps[stagePropCode]?.getBrightness() ?: 100) / 100f
        val globalBrightness = settings.brightness

        return (globalBrightness * propBrightness).toInt()
    }

    private fun buildHeader(clientId: Int, brightness: Int): ByteArray {
        val headerClientId = clientId shl 4 and 0b11110000 // CCCC0000
        val headerBrightness = brightness and 0b00001111 // 0000BBBB
        return byteArrayOf((headerClientId or headerBrightness).toByte()) // CCCCBBBB
    }

    private fun buildFrame(stagePropCode: String, playbackState: PlaybackState): ByteArray {
        return when {
            playbackState.isEmpty -> blackFrame
            else -> {
                val frameCount = SequenceUtils.getFrameCount(playbackState.song!!, playbackState.sequence!!)

                val frameIndex = min(frameCount - 1, round(playbackState.progress * frameCount).toInt())
                val renderedFrame = getRenderedFrame(playbackState, stagePropCode, frameIndex)
                renderedFrame?.getData() ?: blackFrame
            }
        }
    }

    private fun getRenderedFrame(playbackState: PlaybackState, stagePropCode: String, frameIndex: Int): RenderedFrame? {
        val renderedStageProps = playbackState.renderedStageProps
        val stagePropUuid = playbackState.stageProps[stagePropCode]?.getUuid()

        val renderedStagePropData = renderedStageProps!![stagePropUuid]
        val frames = renderedStagePropData?.frames ?: emptyList()
        return if (frameIndex >= frames.size) null else frames[frameIndex]
    }

    private fun buildResponse(header: ByteArray, frameData: ByteArray): ByteArray {
        val headerAndData = ByteArray(header.size + frameData.size)
        System.arraycopy(header, 0, headerAndData, 0, header.size)
        System.arraycopy(frameData, 0, headerAndData, header.size, frameData.size)

        return headerAndData
    }

    companion object {
        const val KEY = "GF"
        private val blackFrame = byteArrayOf(0, 0, 0)
    }
}
