package io.sparkled.udpserver.impl.command

import io.sparkled.model.setting.SettingsCacheEntry
import io.sparkled.music.PlaybackState
import java.net.InetAddress

/**
 * Retrieves the stage prop codes that contain rendered data for the sequence that is currently playing.
 * Command syntax: GP (returns stage prop codes joined by colons, e.g. "P1:P2:P3"
 */
class GetStagePropCodesCommand : UdpCommand {

    override fun handle(
        ipAddress: InetAddress,
        port: Int,
        args: List<String>,
        settings: SettingsCacheEntry,
        playbackState: PlaybackState,
    ): ByteArray {
        val stageProps = playbackState.stageProps.values
        val stagePropCodes = stageProps
            .sortedBy { it.displayOrder }
            .joinToString(":") { it.code }

        return stagePropCodes.toByteArray()
    }

    companion object {
        const val KEY = "GP"
    }
}
