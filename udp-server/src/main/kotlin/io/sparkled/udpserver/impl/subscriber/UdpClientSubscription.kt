package io.sparkled.udpserver.impl.subscriber

data class UdpClientSubscription(
    val stagePropCode: String,
    var timestamp: Long,
    var port: Int
)
