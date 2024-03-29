package io.sparkled.udpserver.impl.command

import io.sparkled.model.render.RenderedFrame
import io.sparkled.model.setting.SettingsCacheEntry
import io.sparkled.model.util.MathUtils
import io.sparkled.music.PlaybackState
import java.net.InetAddress
import kotlin.math.min
import kotlin.math.round

/**
 * Retrieves the rendered stage prop data frame for the current sequence, synchronised to the current point of playback.
 * Returns an empty response if no rendered frame is found for the stage prop.
 * Command syntax: GF:StagePropCode, e.g. GF:P1.
 */
class GetFrameCommand : UdpCommand {

    override fun handle(
        ipAddress: InetAddress,
        port: Int,
        args: List<String>,
        settings: SettingsCacheEntry,
        playbackState: PlaybackState,
    ): ByteArray {
        val stagePropCode = args[1]
        val brightness = calculateBrightness(stagePropCode, settings, playbackState)

        val headerData = buildHeader(brightness)
        val frameData = buildFrame(stagePropCode, playbackState)
        return buildResponse(headerData, frameData)
    }

    private fun calculateBrightness(
        stagePropCode: String,
        settings: SettingsCacheEntry,
        playbackState: PlaybackState,
    ): Int {
        val stageProp = playbackState.stageProps[stagePropCode]
        val propBrightness = (stageProp?.brightness ?: 100) / 100f
        val globalBrightness = MathUtils.map(settings.brightness.toFloat(), 0f, 100f, 0f, 15f).toInt()

        return (globalBrightness * propBrightness).toInt()
    }

    private fun buildHeader(brightness: Int): ByteArray {
        val headerBrightness = brightness and 0b00001111 // 0000BBBB
        return byteArrayOf(headerBrightness.toByte()) // CCCCBBBB
    }

    private fun buildFrame(stagePropCode: String, playbackState: PlaybackState): ByteArray {
        val frameCount = playbackState.frameCount
        val frameIndex = min(frameCount - 1, round(playbackState.progress * frameCount).toInt())

        val renderedFrame = getRenderedFrame(playbackState, stagePropCode, frameIndex)
        return renderedFrame?.getData() ?: blackFrame
    }

    private fun getRenderedFrame(
        playbackState: PlaybackState,
        groupCodeOrStagePropCode: String,
        frameIndex: Int,
    ): RenderedFrame? {
        val renderedStageProps = playbackState.renderedStageProps
        val stagePropId = playbackState.stageProps[groupCodeOrStagePropCode]?.id ?: groupCodeOrStagePropCode

        val renderedStagePropData = renderedStageProps[stagePropId]
        val frames = renderedStagePropData?.frames ?: emptyList()
        return frames.getOrNull(frameIndex)
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
