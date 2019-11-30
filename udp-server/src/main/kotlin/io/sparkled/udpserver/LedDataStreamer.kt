package io.sparkled.udpserver

import java.net.DatagramSocket

/**
 * Sends synchronised LED data to subscribed clients..
 */
interface LedDataStreamer {

    /**
     * Starts the LED streamer.
     * @param socket The socket to listen on.
     */
    fun start(socket: DatagramSocket)

    /**
     * Stops the LED streamer.
     */
    fun stop()
}
