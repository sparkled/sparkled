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
import io.sparkled.model.UniqueId
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.animation.fill.BlendMode
import io.sparkled.model.animation.fill.Fill
import io.sparkled.model.render.Led
import io.sparkled.model.util.ArgumentUtils
import io.sparkled.music.InteractivePlaybackState
import io.sparkled.music.PlaybackService
import io.sparkled.persistence.DbService
import io.sparkled.persistence.cache.CacheService
import io.sparkled.persistence.repository.findByIdOrNull
import io.sparkled.renderer.RenderMode
import io.sparkled.renderer.Renderer
import io.sparkled.renderer.SparkledPluginManager
import io.sparkled.renderer.effect.GlitterEffect
import io.sparkled.renderer.fill.SingleColorFill
import java.lang.System.currentTimeMillis
import java.lang.Thread.sleep
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.system.measureTimeMillis

@ServerWebSocket("/api/websocket")
@Secured(SecurityRule.IS_ANONYMOUS)
class WebSocketServer(
    private val cache: CacheService,
    private val db: DbService,
    private val objectMapper: ObjectMapper,
    private val playbackService: PlaybackService,
    private val pluginManager: SparkledPluginManager,
    private val webSocketBroadcaster: WebSocketBroadcaster,
) {
    private val subscribers = ConcurrentHashMap<String, Long>()
    private val threadPool = Executors.newThreadPerTaskExecutor(NamedVirtualThreadFactory("webSocketServer"))
    private val liveDataRenderRequired = AtomicBoolean(false)

    private val stagePropEffects = mutableMapOf<UniqueId, MutableList<Effect>>()

    init {
        threadPool.execute {
            while (true) {
                try {
                    subscribers.entries.removeIf { currentTimeMillis() - it.value > SUBSCRIPTION_DURATION_MS }
                    val elapsed = measureTimeMillis(::broadcastLiveData)
                    val frameDurationMs = (1000.0 / playbackService.state.framesPerSecond).toInt()
                    sleep((frameDurationMs - elapsed).coerceAtLeast(0))
                } catch (e: Exception) {
                    logger.error("Error occurred during live data broadcast.", e)
                    sleep(100)
                }
            }
        }

        threadPool.execute {
            val startFrame = (currentTimeMillis() / InteractivePlaybackState.FRAMES_PER_SECOND).toInt()

            val effectDurationFrames = 1_000_000_000
            // TODO remove hard-coded effect.
            stagePropEffects["jcyrk6ds8krm"] = mutableListOf(
                Effect(
                    type = GlitterEffect.id,
                    fill = Fill(
                        SingleColorFill.id,
                        BlendMode.NORMAL,
                        mapOf(
                            ArgumentUtils.arg(SingleColorFill.Params.COLOR.name, "#ff00ff")
                        ),
                    ),
                    startFrame = startFrame,
                    endFrame = startFrame + effectDurationFrames,
                    args = mapOf(
                        ArgumentUtils.arg(GlitterEffect.Params.DENSITY.name, 99),
                        ArgumentUtils.arg(GlitterEffect.Params.LIFETIME.name, 4)
                    )
                )
            )

            while (true) {
                try {
                    val playbackState = playbackService.state

                    val elapsed = measureTimeMillis {  }
                    if (playbackState is InteractivePlaybackState) {
                        val currentFrame = currentTimeMillis() / InteractivePlaybackState.FRAMES_PER_SECOND
                        val renderer = Renderer(
                            pluginManager = pluginManager,
                            gifs = { cache.gifs.get() },
                            stage = playbackState.stage,
                            framesPerSecond = 30,
                            stagePropEffects = stagePropEffects,
                            stageProps = playbackState.stageProps.values.associateBy { it.id },
                            startFrame = currentFrame.toInt(),
                            endFrame = currentFrame.toInt() + 1,
                            mode = RenderMode.LIVE_FRAME,
                        )

                        playbackState.renderedStageProps = renderer.render().stageProps
                    }


                    sleep((33 - elapsed).coerceAtLeast(0))
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
                    command.modifications.forEach { modification ->
                        when (modification.type) {
                            LiveDataModificationType.FILL_SOLID -> {
                                val params = objectMapper.convertValue<FillSolidLiveDataParams>(modification.params)
                                val (r, g, b) = params.color.chunked(2).map { it.toInt(16) }

                                state.renderedStageProps.forEach { (groupCode, renderedStageProp) ->
                                    if (params.groupCode == null || params.groupCode == groupCode) {
                                        val renderedFrame = renderedStageProp.frames[0]

                                        repeat(renderedStageProp.data.size / Led.BYTES_PER_LED) { index ->
                                            renderedFrame.getLed(index).setRgb(r, g, b)
                                        }
                                    }
                                }
                            }

                            LiveDataModificationType.SET_PIXELS -> {
                                TODO()
                            }
                        }
                    }

                    liveDataRenderRequired.set(true)
                }
            }

            WebSocketCommandType.LIVE_DATA_RESPONSE -> {
                // This command is only sent as a response, so it doesn't need to be processed.
            }

            WebSocketCommandType.LIVE_DATA_SUBSCRIBE -> {
                subscribers[session.id] = currentTimeMillis()

                if (!subscribers.containsKey(session.id)) {
                    logger.info("Added live update subscriber.", "id" to session.id)
                }
            }

            WebSocketCommandType.LIVE_DATA_UNSUBSCRIBE -> {
                subscribers -= session.id
                logger.info("Removed live update subscriber.", "id" to session.id)
            }

            WebSocketCommandType.PING -> {
                webSocketBroadcaster.broadcastAsync(commandNode) { it.id == session.id }
            }


            WebSocketCommandType.TOGGLE_INTERACTIVE_MODE -> {
                val command = objectMapper.convertValue<ToggleInteractiveModeCommand>(commandNode)

                val state = playbackService.state
                if (command.enabled && state !is InteractivePlaybackState) {
                    val stageId = command.stageId
                        ?: throw RuntimeException("A stage ID must be provided when enabling interactive mode.")
                    val (stage, stageProps) = db.inTransaction {
                        val stage = db.stages.findByIdOrNull(stageId)
                            ?: throw RuntimeException("Stage $stageId not found.")
                        val stageProps = db.stageProps.findAllByStageId(stageId)
                        stage to stageProps
                    }
                    playbackService.enableInteractiveMode(stage, stageProps)
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

    companion object {
        private val logger = getLogger<WebSocketServer>()
        private const val SUBSCRIPTION_DURATION_MS = 5000
    }
}
