package io.sparkled.api.websocket

import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
data class WebSocketCommand(
    val code: WebSocketCommandType,
)

@GenerateClientType
enum class WebSocketCommandType(
    val code: String,
) {
    SUBSCRIBE_TO_LIVE_UPDATES("STLU"),
}