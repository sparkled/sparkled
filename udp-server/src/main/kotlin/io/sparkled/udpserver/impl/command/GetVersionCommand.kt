package io.sparkled.udpserver.impl.command

import io.sparkled.model.setting.SettingsCache
import io.sparkled.music.PlaybackState
import java.net.InetAddress

/**
 * Retrieves the current UDP protocol version. This can be used by clients to determine whether they can support the
 * protocol of the Sparkled instance they are talking to.
 * Command syntax: GV
 */
class GetVersionCommand : UdpCommand {

    override fun handle(
        ipAddress: InetAddress,
        port: Int,
        args: List<String>,
        settings: SettingsCache,
        playbackState: PlaybackState
    ): ByteArray {
        return byteArrayOf(UDP_PROTOCOL_VERSION.toByte())
    }

    companion object {
        const val KEY = "GV"
        private const val UDP_PROTOCOL_VERSION = 1
    }
}
