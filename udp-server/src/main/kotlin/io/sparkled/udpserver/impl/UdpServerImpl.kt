package io.sparkled.udpserver.impl

import io.sparkled.common.logging.getLogger
import io.sparkled.common.threading.NamedVirtualThreadFactory
import io.sparkled.udpserver.RequestHandler
import io.sparkled.udpserver.UdpServer
import jakarta.inject.Singleton
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.nio.charset.StandardCharsets
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

// TODO consider using NIO sockets.
@Singleton
class UdpServerImpl(
    private val requestHandler: RequestHandler,
) : UdpServer {

    private val executor: ExecutorService = Executors.newSingleThreadExecutor(
        NamedVirtualThreadFactory(javaClass.simpleName),
    )

    private val responseExecutor: ExecutorService = Executors.newFixedThreadPool(
        REQUEST_HANDLER_THREADS,
        NamedVirtualThreadFactory("${javaClass.simpleName}RequestHandler"),
    )

    private var started: Boolean = false

    override fun start(socket: DatagramSocket) {
        if (started) {
            logger.warn("Attempted to start UDP server, but it is already running.")
            return
        }

        started = true
        executor.submit { listen(socket) }

        logger.info("Started UDP server.", "port" to socket.localPort)
    }

    override fun stop() {
        started = false
    }

    private fun listen(serverSocket: DatagramSocket) {
        val receiveData = ByteArray(RECEIVE_BUFFER_SIZE)

        while (started) {
            try {
                handleRequest(serverSocket, receiveData)
            } catch (e: Exception) {
                logger.error("Failed to handle UDP request.", e)
            }
        }
    }

    private fun handleRequest(serverSocket: DatagramSocket, receiveData: ByteArray) {
        val receivePacket = DatagramPacket(receiveData, receiveData.size)
        serverSocket.receive(receivePacket)

        val message = String(receivePacket.data, 0, receivePacket.length, StandardCharsets.UTF_8)
        responseExecutor.submit { requestHandler.handle(serverSocket, receivePacket, message) }
    }

    companion object {
        private val logger = getLogger<UdpServerImpl>()
        private const val RECEIVE_BUFFER_SIZE = 32
        private const val REQUEST_HANDLER_THREADS = 4
    }
}
