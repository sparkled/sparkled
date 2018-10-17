package io.sparkled.udpserver.impl;

import io.sparkled.music.PlaybackState;
import io.sparkled.music.PlaybackStateService;
import io.sparkled.udpserver.RequestHandler;
import io.sparkled.udpserver.impl.command.GetFrameCommand;
import io.sparkled.udpserver.impl.command.RequestCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestHandlerImpl implements RequestHandler {

    public static final byte[] ERROR_CODE_BYTES = "ERR".getBytes(StandardCharsets.US_ASCII);
    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerImpl.class);

    // TODO Use Map.ofEntries() after moving to Java 11.
    private final Map<String, RequestCommand> commands = new HashMap<>();
    private final PlaybackStateService playbackStateService;

    @Inject
    public RequestHandlerImpl(PlaybackStateService playbackStateService) {
        this.playbackStateService = playbackStateService;
        commands.put(GetFrameCommand.KEY, new GetFrameCommand());
    }

    @Override
    public void handle(DatagramSocket serverSocket, DatagramPacket receivePacket) {
        try {
            String message = new String(receivePacket.getData()).substring(0, receivePacket.getLength());
            String[] args = message.split(":");
            byte[] response = getResponse(args);
            respond(serverSocket, receivePacket, response);
        } catch (IOException e) {
            logger.error("Failed to handle response.", e);
        }
    }

    private byte[] getResponse(String[] args) {
        PlaybackState playbackState = playbackStateService.getPlaybackState();
        String command = args[0];
        RequestCommand requestCommand = commands.get(command);

        if (playbackState.isEmpty() || requestCommand == null) {
            return ERROR_CODE_BYTES;
        } else {
            return requestCommand.getResponse(args, playbackState);
        }
    }

    private void respond(DatagramSocket serverSocket, DatagramPacket receivePacket, byte[] data) throws IOException {
        InetAddress IPAddress = receivePacket.getAddress();
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, receivePacket.getPort());
        serverSocket.send(sendPacket);
    }
}
