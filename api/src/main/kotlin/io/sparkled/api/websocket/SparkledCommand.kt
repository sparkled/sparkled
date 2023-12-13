package io.sparkled.api.websocket

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.JsonNode
import io.sparkled.model.UniqueId
import io.sparkled.model.annotation.GenerateClientType
import java.time.Instant

@GenerateClientType
interface SparkledCommand {
    val type: WebSocketCommandType
}

@GenerateClientType
data class LiveDataModifyCommand(
    @field:JsonProperty("m")
    val modifications: List<LiveDataModification>,
) : SparkledCommand {
    override val type = WebSocketCommandType.LIVE_DATA_MODIFY
}

@GenerateClientType
data class ToggleInteractiveModeCommand(
    val enabled: Boolean,
    val stageId: UniqueId?,
) : SparkledCommand {
    override val type = WebSocketCommandType.TOGGLE_INTERACTIVE_MODE
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
enum class WebSocketCommandType(
    @field:JsonValue
    val code: String,
) {
    LIVE_DATA_MODIFY("LDM"),
    LIVE_DATA_RESPONSE("LDR"),
    LIVE_DATA_SUBSCRIBE("LDS"),
    LIVE_DATA_UNSUBSCRIBE("LDU"),
    TOGGLE_INTERACTIVE_MODE("TIM"),
    PING("P"),
}

@GenerateClientType
data class LiveDataModification(
    val type: LiveDataModificationType,
    val params: JsonNode,
)

@GenerateClientType
sealed interface LiveDataModificationParams

@GenerateClientType
data class FillSolidLiveDataParams(
    @field:JsonProperty("gc")
    val groupCode: String?,

    @field:JsonProperty("c")
    val color: String,
) : LiveDataModificationParams

@GenerateClientType
data class SetPixelsLiveDataModification(
    @field:JsonProperty("gc")
    val groupCode: String,

    /** Example: 0=ff0000,1-5=00ff00,6=0000ff */
    @field:JsonProperty("d")
    val data: String,
) : LiveDataModificationParams

@GenerateClientType
enum class LiveDataModificationType(
    @field:JsonValue
    val code: String,
) {
    FILL_SOLID("FS"),
    SET_PIXELS("SP"),
}
