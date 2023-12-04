package io.sparkled.api.websocket

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
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
import io.sparkled.persistence.DbService
import java.lang.System.currentTimeMillis
import java.lang.Thread.sleep
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.function.Predicate
import kotlin.system.measureTimeMillis

@ServerWebSocket("/api/websocket")
@Secured(SecurityRule.IS_ANONYMOUS)
class WebSocketServer(
    private val db: DbService,
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
                        sleep((33 - elapsed).coerceAtLeast(0))
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

        val liveData = playbackState.renderedStageProps.entries.associate { (key, value) ->
            val frameIndex = (playbackState.progress * (value.frames.size - 1)).toInt()
            val frameData = value.frames.getOrNull(frameIndex)?.getData() ?: byteArrayOf()
            key to frameData
        }

        val liveDataResponse = LiveDataResponseCommand(liveData)

        subscribers.keys.forEach { sessionId ->
            webSocketBroadcaster.broadcastAsync(liveDataResponse) { it.id == sessionId }
        }
    }

    @OnOpen
    fun onOpen(session: WebSocketSession) {
        logger.info("Websocket opened.", "id" to session.id)
    }

    @OnMessage
    fun onMessage(message: String, session: WebSocketSession) {
        val commandNode = objectMapper.readValue<JsonNode>(message)
        val commandType = enumValues<WebSocketCommandType>().find { it.code == commandNode.get("type").asText() }

        when (commandType) {
            WebSocketCommandType.LIVE_DATA_MODIFY -> {
                val command = objectMapper.convertValue<LiveDataModifyCommand>(commandNode)
                val state = playbackService.state
                if (state is InteractivePlaybackState) {
                    command.stageProps
                }
            }

            WebSocketCommandType.LIVE_DATA_RESPONSE -> {
                // This command is only sent as a response, so it doesn't need to be processed.
            }

            WebSocketCommandType.LIVE_DATA_SUBSCRIBE -> {
                val action = if (subscribers.containsKey(session.id)) "Updated" else "Added"
                subscribers[session.id] = currentTimeMillis()
                logger.info("$action live update subscriber.", "id" to session.id)
            }

            WebSocketCommandType.LIVE_DATA_UNSUBSCRIBE -> {
                subscribers -= session.id
                logger.info("Removed live update subscriber.", "id" to session.id)
            }

            WebSocketCommandType.PING -> {
                webSocketBroadcaster.broadcast(commandNode, isSameSession(session))
            }

            WebSocketCommandType.TOGGLE_INTERACTIVE_MODE -> {
                val command = objectMapper.convertValue<ToggleInteractiveModeCommand>(commandNode)

                val state = playbackService.state
                if (command.enabled && state !is InteractivePlaybackState) {
                    val stageId = command.stageId
                        ?: throw RuntimeException("A stage ID must be provided when enabling interactive mode.")
                    val stageProps = db.stageProps.findAllByStageId(stageId)
                    playbackService.enableInteractiveMode(stageProps)
                } else if (!command.enabled && state is InteractivePlaybackState) {
                    playbackService.disableInteractiveMode()
                }
            }

            null -> {
                // Command not recognised, ignore.
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

    private fun isSameSession(session: WebSocketSession?) = Predicate<WebSocketSession> {
        it === session
    }

    companion object {
        private val logger = getLogger<WebSocketServer>()
        private const val SUBSCRIPTION_DURATION_MS = 5000
    }
}
