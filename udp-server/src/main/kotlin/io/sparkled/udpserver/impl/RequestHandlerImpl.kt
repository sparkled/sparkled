package io.sparkled.udpserver.impl

import io.sparkled.model.setting.SettingsCache
import io.sparkled.music.PlaybackState
import io.sparkled.music.PlaybackStateService
import io.sparkled.persistence.setting.SettingPersistenceService
import io.sparkled.udpserver.RequestHandler
import io.sparkled.udpserver.impl.command.GetFrameCommand
import io.sparkled.udpserver.impl.command.GetStagePropCodesCommand
import io.sparkled.udpserver.impl.command.GetVersionCommand
import io.sparkled.udpserver.impl.command.RequestCommand
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import javax.inject.Inject
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress
import java.nio.charset.StandardCharsets
import java.util.HashMap

class RequestHandlerImpl @Inject
constructor(private val playbackStateService: PlaybackStateService,
            private val settingPersistenceService: SettingPersistenceService) : RequestHandler {

    // TODO Use Map.ofEntries() after moving to Java 11.
    private val commands = HashMap()

    init {

        commands.put(GetFrameCommand.KEY, GetFrameCommand())
        commands.put(GetStagePropCodesCommand.KEY, GetStagePropCodesCommand())
        commands.put(GetVersionCommand.KEY, GetVersionCommand())
    }

    @Override
    fun handle(serverSocket: DatagramSocket, receivePacket: DatagramPacket) {
        try {
            val message = String(receivePacket.getData()).substring(0, receivePacket.getLength())
            val args = message.split(":")
            val response = getResponse(args)
            respond(serverSocket, receivePacket, response)
        } catch (e: IOException) {
            logger.error("Failed to handle response.", e)
        }

    }

    private fun getResponse(args: Array<String>): ByteArray {
        val playbackState = playbackStateService.getPlaybackState()
        val settings = settingPersistenceService.getSettings()

        val command = args[0]
        val requestCommand = commands.get(command)

        if (requestCommand == null) {
            return ERROR_CODE_BYTES
        } else {
            return requestCommand!!.getResponse(args, settings, playbackState)
        }
    }

    @Throws(IOException::class)
    private fun respond(serverSocket: DatagramSocket, receivePacket: DatagramPacket, data: ByteArray) {
        val IPAddress = receivePacket.getAddress()
        val sendPacket = DatagramPacket(data, data.size, IPAddress, receivePacket.getPort())
        serverSocket.send(sendPacket)
    }

    companion object {

        val ERROR_CODE_BYTES = "ERR".getBytes(StandardCharsets.US_ASCII)
        private val logger = LoggerFactory.getLogger(RequestHandlerImpl::class.java)
    }
}
