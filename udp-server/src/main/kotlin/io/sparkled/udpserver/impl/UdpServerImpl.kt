package io.sparkled.udpserver.impl

import com.google.common.util.concurrent.ThreadFactoryBuilder
import io.sparkled.udpserver.RequestHandler
import io.sparkled.udpserver.UdpServer
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.nio.charset.StandardCharsets
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import javax.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class UdpServerImpl(private val requestHandler: RequestHandler) : UdpServer {

    private val executor: ExecutorService = Executors.newSingleThreadExecutor(
        ThreadFactoryBuilder().setNameFormat("udp-server-%d").build()
    )

    private val responseExecutor: ExecutorService = Executors.newFixedThreadPool(
        REQUEST_HANDLER_THREADS, ThreadFactoryBuilder().setNameFormat("request-handler-%d").build()
    )

    private var started: Boolean = false

    override fun start(socket: DatagramSocket) {
        if (started) {
            logger.warn("Attempted to start UDP server, but it is already running.")
            return
        }

        started = true
        executor.submit { listen(socket) }

        logger.info("Started UDP server at port {}.", socket.port)
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
        private val logger = LoggerFactory.getLogger(UdpServerImpl::class.java)
        private const val RECEIVE_BUFFER_SIZE = 32
        private const val REQUEST_HANDLER_THREADS = 4
    }
}
