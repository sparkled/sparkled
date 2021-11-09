package io.sparkled.udpserver.impl.subscriber

import java.net.InetAddress
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Singleton

@Singleton
class UdpClientSubscribers : ConcurrentHashMap<InetAddress, UdpClientSubscription>()
