package io.sparkled.udpserver.impl.subscriber

import jakarta.inject.Singleton
import java.net.InetAddress
import java.util.concurrent.ConcurrentHashMap

@Singleton
class UdpClientSubscribers : ConcurrentHashMap<InetAddress, UdpClientSubscription>()
