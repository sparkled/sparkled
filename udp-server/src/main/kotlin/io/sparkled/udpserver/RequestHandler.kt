package io.sparkled.udpserver

import java.net.DatagramPacket
import java.net.DatagramSocket

/**
 * Returns appropriate responses to UDP requests.
 */
interface RequestHandler {

    fun handle(serverSocket: DatagramSocket, receivePacket: DatagramPacket)
}
