package io.sparkled.udpserver.impl.command

import io.sparkled.model.setting.SettingsCache
import io.sparkled.music.PlaybackState

import java.nio.charset.StandardCharsets
import java.util.stream.Collectors

/**
 * Retrieves the stage prop codes that contain rendered data for the sequence that is currently playing.
 * UDP protocol version. This can be used by clients to determine whether they can support the
 * protocol of the Sparkled instance they are talking to.
 * Command syntax: GP (returns stage prop codes joined by colons, e.g. "P1:P2:P3"
 */
class GetStagePropCodesCommand : RequestCommand() {

    @Override
    fun getResponse(args: Array<String>, settings: SettingsCache, playbackState: PlaybackState): ByteArray {
        if (playbackState.isEmpty()) {
            return getErrorResponse()
        }

        return playbackState.getStagePropUuids()
                .keySet()
                .stream()
                .collect(Collectors.joining(":"))
                .getBytes(StandardCharsets.UTF_8)
    }

    companion object {

        val KEY = "GP"
    }
}
