package io.sparkled.udpserver.impl

import io.sparkled.model.constant.ModelConstants.MS_PER_SECOND
import io.sparkled.music.PlaybackStateService
import io.sparkled.persistence.cache.CacheService
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
    private val caches: CacheService,
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
                val settings = caches.settings.get()
                val playbackState = playbackStateService.getPlaybackState()

                try {
                    if (subscribers.isEmpty()) {
                        delay(100)
                        continue
                    }

                    subscribers.forEach { subscriberIp, it ->
                        if (iterationTime - it.timestamp < SUBSCRIBER_TIMEOUT_MS) {
                            val args = listOf("GF", it.stagePropCode)
                            val ledData: ByteArray =
                                GetFrameCommand().handle(subscriberIp, it.port, args, settings, playbackState)
                            val sendPacket = DatagramPacket(ledData, ledData.size, subscriberIp, it.port)
                            socket.send(sendPacket)
                        }
                    }

                    if (System.currentTimeMillis() - iterationTime > 5) {
                        println("${System.currentTimeMillis()} Iteration took ${System.currentTimeMillis() - iterationTime} ms")
                    }
                    val elapsedMs = System.currentTimeMillis() - iterationTime
                    val updateInterval = MS_PER_SECOND / (playbackState.sequence?.framesPerSecond ?: 10)
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
        private const val SUBSCRIBER_TIMEOUT_MS = 30 * MS_PER_SECOND
    }
}
