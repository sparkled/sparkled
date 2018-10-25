package io.sparkled.udpserver.impl

import com.google.common.util.concurrent.ThreadFactoryBuilder
import io.sparkled.udpserver.RequestHandler
import io.sparkled.udpserver.UdpServer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Inject
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.SocketException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UdpServerImpl @Inject
constructor(private val requestHandler: RequestHandler) : UdpServer {
    private val executor: ExecutorService
    private var started: Boolean = false

    init {
        this.executor = Executors.newSingleThreadExecutor(
                ThreadFactoryBuilder().setNameFormat("udp-server-%d").build()
        )
    }

    @Override
    @Throws(SocketException::class)
    fun start(port: Int) {
        if (started) {
            logger.warn("Attempted to start UDP server, but it is already running.")
            return
        }

        val serverSocket = DatagramSocket(port)
        executor.submit({ listen(serverSocket) })

        started = true
        logger.info("Started UDP server at port {}.", port)
    }

    private fun listen(serverSocket: DatagramSocket): Object {
        val receiveData = ByteArray(RECEIVE_BUFFER_SIZE)

        while (true) {
            try {
                handleRequest(serverSocket, receiveData)
            } catch (e: Exception) {
                logger.error("Failed to handle UDP request.", e)
            }

        }
    }

    @Throws(IOException::class)
    private fun handleRequest(serverSocket: DatagramSocket, receiveData: ByteArray) {
        val receivePacket = DatagramPacket(receiveData, receiveData.size)
        serverSocket.receive(receivePacket)
        requestHandler.handle(serverSocket, receivePacket)
    }

    companion object {

        private val logger = LoggerFactory.getLogger(UdpServerImpl::class.java)
        private val RECEIVE_BUFFER_SIZE = 20
    }
}
