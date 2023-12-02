package io.sparkled.api.websocket

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import io.sparkled.model.annotation.GenerateClientType
import java.time.Instant

@GenerateClientType
interface SparkledCommand {
    val type: WebSocketCommandType
}

@GenerateClientType
data class LiveDataModifyCommand(
    val stageProps: Map<String, List<LiveDataModification>>,
) : SparkledCommand {
    override val type = WebSocketCommandType.LIVE_DATA_MODIFY
}

@GenerateClientType
data class LiveDataResponseCommand(
    /**
     * A map keyed by stage prop code (or stage prop group code), where the value is the current live data for that
     * stage prop/group.
     */
    val data: Map<String, ByteArray>,
) : SparkledCommand {
    override val type = WebSocketCommandType.LIVE_DATA_RESPONSE
}

@GenerateClientType
data object LiveDataSubscribeCommand : SparkledCommand {
    override val type = WebSocketCommandType.LIVE_DATA_SUBSCRIBE
}

@GenerateClientType
data object LiveDataUnsubscribeCommand : SparkledCommand {
    override val type = WebSocketCommandType.LIVE_DATA_UNSUBSCRIBE
}

@GenerateClientType
data class PingCommand(
    val ts: Instant,
) : SparkledCommand {
    override val type = WebSocketCommandType.PING
}

@GenerateClientType
data class LiveDataModification(
    @JsonProperty("i")
    val index: Int,

    @JsonProperty("c")
    val color: Int,
)

@GenerateClientType
enum class WebSocketCommandType(
    @field:JsonValue
    val code: String,
) {
    LIVE_DATA_MODIFY("LDM"),
    LIVE_DATA_RESPONSE("LDR"),
    LIVE_DATA_SUBSCRIBE("LDS"),
    LIVE_DATA_UNSUBSCRIBE("LDU"),
    PING("P"),
}
