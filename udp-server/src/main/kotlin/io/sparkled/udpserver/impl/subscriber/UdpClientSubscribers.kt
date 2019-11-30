package io.sparkled.udpserver.impl.subscriber

import java.net.InetAddress
import javax.inject.Singleton

@Singleton
class UdpClientSubscribers : HashMap<InetAddress, MutableList<UdpClientSubscription>>()
