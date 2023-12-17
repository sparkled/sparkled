package io.sparkled.api.websocket

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import io.sparkled.model.UniqueId
import io.sparkled.model.animation.effect.Effect
import io.sparkled.model.annotation.GenerateClientType
import io.sparkled.viewmodel.Point2dViewModel
import java.time.Instant
import java.util.Queue

@GenerateClientType
interface SparkledCommand {
    val type: SparkledCommandType
}

@GenerateClientType
data object LiveDataClearCommand : SparkledCommand {
    override val type = SparkledCommandType.LIVE_DATA_MODIFY
}

@GenerateClientType
data class LiveDataModifyCommand(
    @field:JsonProperty("e")
    val effect: Effect,

    @field:JsonProperty("tp")
    val touchPoints: List<Point2dViewModel>,

    @field:JsonProperty("d")
    val distance: Double,
) : SparkledCommand {
    override val type = SparkledCommandType.LIVE_DATA_MODIFY
}

@GenerateClientType
data class ToggleInteractiveModeCommand(
    val enabled: Boolean,
    val stageId: UniqueId?,
) : SparkledCommand {
    override val type = SparkledCommandType.TOGGLE_INTERACTIVE_MODE
}

@GenerateClientType
data class LiveDataResponseCommand(
    /**
     * A map keyed by stage prop code (or stage prop group code), where the value is the current live data for that
     * stage prop/group.
     */
    val data: Map<String, ByteArray>,
) : SparkledCommand {
    override val type = SparkledCommandType.LIVE_DATA_RESPONSE
}

@GenerateClientType
data object LiveDataSubscribeCommand : SparkledCommand {
    override val type = SparkledCommandType.LIVE_DATA_SUBSCRIBE
}

@GenerateClientType
data object LiveDataUnsubscribeCommand : SparkledCommand {
    override val type = SparkledCommandType.LIVE_DATA_UNSUBSCRIBE
}

@GenerateClientType
data class PingCommand(
    val ts: Instant,
) : SparkledCommand {
    override val type = SparkledCommandType.PING
}

@GenerateClientType
enum class SparkledCommandType(
    @field:JsonValue
    val code: String,
) {
    LIVE_DATA_CLEAR("LDC"),
    LIVE_DATA_MODIFY("LDM"),
    LIVE_DATA_RESPONSE("LDR"),
    LIVE_DATA_SUBSCRIBE("LDS"),
    LIVE_DATA_UNSUBSCRIBE("LDU"),
    TOGGLE_INTERACTIVE_MODE("TIM"),
    PING("P"),
}
