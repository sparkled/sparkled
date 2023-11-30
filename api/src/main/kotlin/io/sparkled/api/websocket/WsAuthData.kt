package io.sparkled.api.websocket

import au.telenp.service.assessment.LiveAssessmentContext
import java.net.InetSocketAddress

data class WsAuthData(
    val context: LiveAssessmentContext,
    val ipAddress: InetSocketAddress,
    val timestamp: Long = System.currentTimeMillis()
)
