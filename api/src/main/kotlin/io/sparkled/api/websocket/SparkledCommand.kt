package io.sparkled.api.websocket

import com.fasterxml.jackson.annotation.JsonProperty
import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
interface SparkledCommand {
    val code: WebSocketCommandType
}

@GenerateClientType
data class UpdateLiveDataCommand(
    val stageProps: Map<String, List<LiveDataUpdate>>
) : SparkledCommand {
    override val code = WebSocketCommandType.UPDATE_LIVE_DATA
}

@GenerateClientType
data class LiveDataUpdate(
    @JsonProperty("i")
    val index: Int,

    @JsonProperty("c")
    val color: Int,
)

@GenerateClientType
enum class WebSocketCommandType(
    val code: String,
) {
    SUBSCRIBE_TO_LIVE_UPDATES("STLU"),
    UPDATE_LIVE_DATA("ULD"),
}
