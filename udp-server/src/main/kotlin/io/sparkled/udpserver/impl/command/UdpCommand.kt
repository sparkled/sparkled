package io.sparkled.udpserver.impl.command

import io.sparkled.model.setting.SettingsCache
import io.sparkled.music.PlaybackState
import java.net.InetAddress

interface UdpCommand {

    fun handle(
        ipAddress: InetAddress,
        port: Int,
        args: List<String>,
        settings: SettingsCache,
        playbackState: PlaybackState
    ): ByteArray?
}
