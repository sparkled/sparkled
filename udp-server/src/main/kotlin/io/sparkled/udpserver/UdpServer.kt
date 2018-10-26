package io.sparkled.udpserver

import java.net.SocketException

/**
 * A UDP Server that listens on the given port.
 */
interface UdpServer {

    /**
     * Start the UDP server on the provided port. This method is idempotent.

     * @param port The port to listen on.
     */
    @Throws(SocketException::class)
    fun start(port: Int)
}
