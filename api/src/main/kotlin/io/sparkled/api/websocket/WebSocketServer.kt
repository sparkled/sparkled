package io.sparkled.api.websocket

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.websocket.WebSocketBroadcaster
import io.micronaut.websocket.WebSocketSession
import io.micronaut.websocket.annotation.OnClose
import io.micronaut.websocket.annotation.OnError
import io.micronaut.websocket.annotation.OnMessage
import io.micronaut.websocket.annotation.OnOpen
import io.micronaut.websocket.annotation.ServerWebSocket
import io.sparkled.common.logging.getLogger
import io.sparkled.common.threading.NamedVirtualThreadFactory
import io.sparkled.music.InteractivePlaybackState
import io.sparkled.music.PlaybackService
import java.lang.System.currentTimeMillis
import java.lang.Thread.sleep
import java.util.Base64
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis

@ServerWebSocket("/websocket")
@Secured(SecurityRule.IS_ANONYMOUS)
class WebSocketServer(
    private val objectMapper: ObjectMapper,
    private val playbackService: PlaybackService,
    private val webSocketBroadcaster: WebSocketBroadcaster,
) {
    private val subscribers = ConcurrentHashMap<String, Long>()
    private val threadPool = Executors.newThreadPerTaskExecutor(NamedVirtualThreadFactory("webSocketServer"))

    init {
        threadPool.execute {
            while (true) {
                try {
                    if (subscribers.isEmpty()) {
                        sleep(500)
                    } else {
                        subscribers.entries.removeIf { currentTimeMillis() - it.value > SUBSCRIPTION_DURATION_MS }

                        // TODO calculate time to sleep depending on playback type.
                        val elapsed = measureTimeMillis(::broadcastLiveData)
                        sleep((10 - elapsed).coerceAtLeast(0))
                    }
                } catch (e: Exception) {
                    logger.error("Error occurred during live data broadcast.", e)
                    sleep(100)
                }
            }
        }
    }

    private fun broadcastLiveData() {
        val playbackState = playbackService.state

        val liveData = playbackState.renderedStageProps.map {
            val frameIndex = (it.value.frames.size * playbackState.progress).toInt()
            val frameData = it.value.frames.getOrNull(frameIndex)?.getData()
            it.key to Base64.getEncoder().encode(frameData)
        }

        subscribers.keys.forEach { sessionId ->
            webSocketBroadcaster.broadcastAsync(liveData) { it.id == sessionId }
        }
    }

    @OnOpen
    fun onOpen(session: WebSocketSession) {
        logger.info("Session opened.", "id" to session.id)
    }

    @OnMessage
    fun onMessage(message: String, session: WebSocketSession) {
        val command = objectMapper.readValue<SparkledCommand>(message)
        when (command.code) {
            WebSocketCommandType.SUBSCRIBE_TO_LIVE_UPDATES -> {
                subscribers[session.id] = currentTimeMillis()
                logger.info("Added live update subscriber.", "id" to session.id)
            }

            WebSocketCommandType.UPDATE_LIVE_DATA -> {
                with (playbackService.state) {
                    if (this is InteractivePlaybackState) {
                        TODO()
                    }
                }

                subscribers[session.id] = currentTimeMillis()
                logger.info("Added live update subscriber.", "id" to session.id)
            }

        }
    }

    @OnClose
    fun onClose(session: WebSocketSession) {
        subscribers -= session.id
        logger.info("Websocket closed.", "id" to session.id)
    }

    @OnError
    fun onError(session: WebSocketSession) {
        logger.error("A websocket error occurred.", "id" to session.id)
    }

    companion object {
        private val logger = getLogger<WebSocketServer>()
        private const val SUBSCRIPTION_DURATION_MS = 5000
    }
}
