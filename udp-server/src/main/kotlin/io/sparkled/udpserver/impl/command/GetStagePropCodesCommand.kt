package io.sparkled.udpserver.impl.command

import io.sparkled.model.setting.SettingsCache
import io.sparkled.music.PlaybackState
import java.nio.charset.StandardCharsets.UTF_8

/**
 * Retrieves the stage prop codes that contain rendered data for the sequence that is currently playing.
 * UDP protocol version. This can be used by clients to determine whether they can support the
 * protocol of the Sparkled instance they are talking to.
 * Command syntax: GP (returns stage prop codes joined by colons, e.g. "P1:P2:P3"
 */
class GetStagePropCodesCommand : RequestCommand() {

    override fun getResponse(args: List<String>, settings: SettingsCache, playbackState: PlaybackState): ByteArray {
        if (playbackState.isEmpty) {
            return emptyResponse
        }

        val props = playbackState.stagePropUuids.keys.joinToString(":")
        val bytes = props.toByteArray(UTF_8)
        return if (bytes.isEmpty()) ByteArray(0) else bytes
    }

    companion object {
        const val KEY = "GP"
    }
}
