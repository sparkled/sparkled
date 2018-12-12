package io.sparkled.udpserver.impl

import io.sparkled.music.PlaybackStateService
import io.sparkled.persistence.setting.SettingPersistenceService
import io.sparkled.udpserver.RequestHandler
import io.sparkled.udpserver.impl.command.GetFrameCommand
import io.sparkled.udpserver.impl.command.GetStagePropCodesCommand
import io.sparkled.udpserver.impl.command.GetVersionCommand
import org.slf4j.LoggerFactory
import java.net.DatagramPacket
import java.net.DatagramSocket
import javax.inject.Inject

class RequestHandlerImpl
@Inject constructor(
    private val playbackStateService: PlaybackStateService,
    private val settingPersistenceService: SettingPersistenceService
) : RequestHandler {

    private val commands = mapOf(
        GetFrameCommand.KEY to GetFrameCommand(),
        GetStagePropCodesCommand.KEY to GetStagePropCodesCommand(),
        GetVersionCommand.KEY to GetVersionCommand()
    )

    override fun handle(serverSocket: DatagramSocket, receivePacket: DatagramPacket, message: String) {
        try {
            val args = message.split(":")
            val response = getResponse(args)
            respond(serverSocket, receivePacket, response)
        } catch (e: Exception) {
            logger.error("Failed to handle response for message '$message': ${e.message}")
        }
    }

    private fun getResponse(args: List<String>): ByteArray {
        val playbackState = playbackStateService.getPlaybackState()
        val settings = settingPersistenceService.settings

        val command = args[0]
        val requestCommand = commands[command] ?: throw IllegalArgumentException("Unrecognised command '$command'.")
        return requestCommand.getResponse(args, settings, playbackState)
    }

    private fun respond(serverSocket: DatagramSocket, receivePacket: DatagramPacket, data: ByteArray) {
        val ipAddress = receivePacket.address
        val sendPacket = DatagramPacket(data, data.size, ipAddress, receivePacket.port)

        serverSocket.send(sendPacket)
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RequestHandlerImpl::class.java)
    }
}
