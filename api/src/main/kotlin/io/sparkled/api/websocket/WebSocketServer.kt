package io.sparkled.api.websocket

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.convertValue
import com.fasterxml.jackson.module.kotlin.readValue
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.micronaut.websocket.CloseReason
import io.micronaut.websocket.WebSocketBroadcaster
import io.micronaut.websocket.WebSocketSession
import io.micronaut.websocket.annotation.OnClose
import io.micronaut.websocket.annotation.OnError
import io.micronaut.websocket.annotation.OnMessage
import io.micronaut.websocket.annotation.OnOpen
import io.micronaut.websocket.annotation.ServerWebSocket
import io.sparkled.common.logging.getLogger
import io.sparkled.common.threading.NamedVirtualThreadFactory
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.embedded.Point2d
import io.sparkled.music.InteractivePlaybackState
import io.sparkled.music.PlaybackService
import io.sparkled.persistence.DbService
import io.sparkled.persistence.cache.CacheService
import io.sparkled.persistence.repository.findByIdOrNull
import io.sparkled.renderer.RenderMode
import io.sparkled.renderer.Renderer
import io.sparkled.renderer.SparkledPluginManager
import io.sparkled.viewmodel.CircleViewModel
import java.lang.System.currentTimeMillis
import java.lang.Thread.sleep
import java.util.BitSet
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.math.pow
import kotlin.math.sqrt
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
    private var running = false
    private val subscribers = ConcurrentHashMap<String, Long>()
    private val threadPool = Executors.newThreadPerTaskExecutor(NamedVirtualThreadFactory(javaClass.simpleName))
    private val pendingCommands = ConcurrentLinkedQueue<LiveDataModifyCommand>()
    private val isClearRequested = AtomicBoolean(false)

    fun start() {
        threadPool.execute(::initLiveData)
        running = true
    }

    private fun initLiveData() {
        while (true) {
            try {
                renderLiveFrame()
            } catch (e: Exception) {
                logger.error("Error occurred during live data render.", e)
                sleep(100)
            }

            try {
                broadcastLiveFrame()
            } catch (e: Exception) {
                logger.error("Error occurred during live data broadcast.", e)
                sleep(100)
            }
        }
    }

    private fun renderLiveFrame() {
        val playbackState = playbackService.state

        val elapsedMs = measureTimeMillis {
            if (playbackState is InteractivePlaybackState) {
                if (isClearRequested.get()) {
                    pendingCommands.clear()
                    playbackState.stagePropEffects.values.forEach { it.clear() }
                    isClearRequested.set(false)
                }

                while (pendingCommands.isNotEmpty()) {
                    addLiveEffect(playbackState)
                }

                val currentFrame = playbackState.getFrameAtCurrentTime()
                val renderer = Renderer(
                    pluginManager = pluginManager,
                    gifs = { cache.gifs.get() },
                    stage = playbackState.stage,
                    framesPerSecond = playbackState.framesPerSecond,
                    stagePropEffects = playbackState.stagePropEffects,
                    stageProps = playbackState.stageProps.values.associateBy { it.id },
                    startFrame = currentFrame,
                    endFrame = currentFrame + 1,
                    mode = RenderMode.LIVE_FRAME,
                )

                playbackState.renderedStageProps = renderer.render().stageProps
            }
        }


        val frameDurationMs = (1000 / playbackState.framesPerSecond) - elapsedMs
        sleep(frameDurationMs.coerceAtLeast(0))
    }

    private fun addLiveEffect(playbackState: InteractivePlaybackState) {
        val command = pendingCommands.poll()
        playbackState.stagePropEffects.forEach { (id, effects) ->
            if (!isSameEffect(effects.lastOrNull(), command.effect)) {
                effects += command.effect.copy(
                    startFrame = playbackState.startFrame,
                    endFrame = playbackState.startFrame + 9999999,
                    targetPixels = BitSet(100),
                )
            }

            val lastEffect = effects.last()

            val stageProp = playbackState.stageProps[id]
            val pixelPositions = stageProp?.ledPositions ?: emptyList()
            pixelPositions.forEachIndexed { index, pixelPosition ->
                val pixelIndex = when (stageProp?.reverse) {
                    true -> pixelPositions.lastIndex - index
                    else -> index
                }
                if (lastEffect.targetPixels?.get(pixelIndex) == true) {
                    // Already present, skip.
                } else if (command.points.any { point -> isInCircle(point, pixelPosition) }) {
                    lastEffect.targetPixels?.set(pixelIndex)
                }
            }
        }
    }

    private fun broadcastLiveFrame() {
        subscribers.entries.removeIf { currentTimeMillis() - it.value > SUBSCRIPTION_DURATION_MS }
        val elapsedMs = measureTimeMillis(::broadcastLiveData)
        val frameDurationMs = (1000.0 / playbackService.state.framesPerSecond).toInt()
        sleep((frameDurationMs - elapsedMs).coerceAtLeast(0))
    }

    /**
     * Broadcasts live data to websocket clients. The live data might be sequence that is playing, or the current
     * interactive mode data. Note that this broadcast is primarily for the Sparkled web application, and other Sparkled
     * clients will generally use the UDP data stream.
     */
    private fun broadcastLiveData() {
        val sessionIds = subscribers.keys

        if (sessionIds.isNotEmpty()) {
            val playbackState = playbackService.state

            val liveData = playbackState.renderedStageProps.entries.associate { (key, value) ->
                val frameIndex = (playbackState.progress * value.frames.lastIndex).toInt()
                val frameData = value.frames.getOrNull(frameIndex)?.getData() ?: byteArrayOf()
                key to frameData
            }

            val liveDataResponse = LiveDataResponseCommand(liveData)

            sessionIds.forEach { sessionId ->
                webSocketBroadcaster.broadcastAsync(liveDataResponse) { it.id == sessionId }
            }
        }
    }

    @OnOpen
    fun onOpen(session: WebSocketSession) {
        if (!running) {
            logger.warn("Rejected Websocket connection, server isn't running yet.", "id" to session.id)
            session.close(CloseReason.TRY_AGAIN_LATER)
        } else {
            logger.info("Websocket opened.", "id" to session.id)
        }
    }

    /**
     * Compares two effects without taking into account the unique ID or target pixels.
     */
    private fun isSameEffect(a: Effect?, b: Effect?): Boolean {
        return when {
            a == null || b == null -> false
            a.type != b.type -> false
            a.easing != b.easing -> false
            a.fill != b.fill -> false
            a.repetitions != b.repetitions -> false
            a.repetitionSpacing != b.repetitionSpacing -> false
            a.args != b.args -> false
            else -> true
        }
    }

    @OnMessage
    fun onMessage(message: String, session: WebSocketSession) {
        val commandNode = objectMapper.readValue<JsonNode>(message)
        val commandType = enumValues<WebSocketCommandType>().find { it.code == commandNode.get("type").asText() }

        when (commandType) {
            WebSocketCommandType.LIVE_DATA_CLEAR -> {
                if (playbackService.state is InteractivePlaybackState) {
                    isClearRequested.set(true)
                }
            }

            WebSocketCommandType.LIVE_DATA_MODIFY -> {
                if (playbackService.state is InteractivePlaybackState) {
                    pendingCommands += objectMapper.convertValue<LiveDataModifyCommand>(commandNode)
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

    private fun isInCircle(circle: CircleViewModel, point: Point2d): Boolean {
        val distance = sqrt(((circle.x - point.x).pow(2)) + ((circle.y - point.y).pow(2)))
        return distance <= circle.r
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
