package io.sparkled.udpserver.impl.command

import io.sparkled.model.setting.SettingsCache
import io.sparkled.music.PlaybackState
import io.sparkled.udpserver.impl.subscriber.UdpClientSubscribers
import io.sparkled.udpserver.impl.subscriber.UdpClientSubscription
import java.net.InetAddress

/**
 * Notifies the server that a client is interested in receiving data for a stage prop. No response is returned to
 * the client.
 * Command syntax: S:StagePropCode, e.g. S:P1.
 */
class SubscribeCommand(private val subscribers: UdpClientSubscribers) : UdpCommand {

    override fun handle(
        ipAddress: InetAddress,
        port: Int,
        args: List<String>,
        settings: SettingsCache,
        playbackState: PlaybackState
    ): ByteArray? {
        // TODO error handling for malformed request.
        val stagePropCode = args[1]

        subscribers.compute(ipAddress) { _, v ->
            v?.copy(timestamp = System.currentTimeMillis())
                ?: UdpClientSubscription(stagePropCode, System.currentTimeMillis(), port)
        }

        return null
    }

    companion object {
        const val KEY = "S"
    }
}
