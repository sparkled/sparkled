package io.sparkled.udpserver.impl.command

import io.sparkled.model.setting.SettingsCache
import io.sparkled.music.PlaybackState

/**
 * Retrieves the current UDP protocol version. This can be used by clients to determine whether they can support the
 * protocol of the Sparkled instance they are talking to.
 * Command syntax: GV
 */
class GetVersionCommand : RequestCommand() {

    @Override
    fun getResponse(args: Array<String>, settings: SettingsCache, playbackState: PlaybackState): ByteArray {
        return byteArrayOf(UDP_PROTOCOL_VERSION.toByte())
    }

    companion object {

        val KEY = "GV"
        private val UDP_PROTOCOL_VERSION = 1
    }
}
