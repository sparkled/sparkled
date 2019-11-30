package io.sparkled.udpserver.impl

import io.sparkled.music.PlaybackStateService
import io.sparkled.persistence.setting.SettingPersistenceService
import io.sparkled.udpserver.LedDataStreamer
import io.sparkled.udpserver.impl.command.GetFrameCommand
import io.sparkled.udpserver.impl.subscriber.UdpClientSubscribers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory
import java.net.DatagramPacket
import java.net.DatagramSocket
import javax.inject.Singleton

@Singleton
class LedDataStreamerImpl(
    private val playbackStateService: PlaybackStateService,
    private val settingPersistenceService: SettingPersistenceService,
    private val subscribers: UdpClientSubscribers
) : LedDataStreamer {

    private var started: Boolean = false

    override fun start(socket: DatagramSocket) {
        if (started) {
            logger.warn("Attempted to start LED data streamer, but it is already running.")
        } else {
            started = true
            GlobalScope.launch { streamData(socket) }
            logger.info("Started LED data streamer.")
        }
    }

    override fun stop() {
        started = false
    }

    private suspend fun streamData(socket: DatagramSocket) {
        try {
            while (started) {
                val iterationTime = System.currentTimeMillis()
                val settings = settingPersistenceService.settings
                val playbackState = playbackStateService.getPlaybackState()

                try {
                    subscribers.forEach { subscriber ->
                        subscriber.value.forEach {
                            if (iterationTime - it.timestamp < SUBSCRIBER_TIMEOUT_MS) {
                                val args = listOf("GF", it.stagePropCode, it.clientId.toString())
                                val ledData: ByteArray = GetFrameCommand().handle(subscriber.key, it.port, args, settings, playbackState)
                                val ipAddress = subscriber.key
                                val sendPacket = DatagramPacket(ledData, ledData.size, ipAddress, it.port)
                                socket.send(sendPacket)
                            }
                        }
                    }

                    if (System.currentTimeMillis() - iterationTime > 5) {
                        println("${System.currentTimeMillis()} Iteration took ${System.currentTimeMillis() - iterationTime} ms")
                    }
                    val elapsedMs = System.currentTimeMillis() - iterationTime
                    val updateInterval = 1000 / (playbackState.sequence?.getFramesPerSecond() ?: 10)
                    delay(updateInterval - elapsedMs)
                } catch (e: Exception) {
                    logger.error("Failed to send LED data to subscriber.", e)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private val logger = LoggerFactory.getLogger(LedDataStreamerImpl::class.java)
        private const val SUBSCRIBER_TIMEOUT_MS = 30000
    }
}
