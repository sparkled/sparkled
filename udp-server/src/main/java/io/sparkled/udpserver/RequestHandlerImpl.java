package io.sparkled.udpserver;

import io.sparkled.model.entity.Song;
import io.sparkled.model.render.RenderedChannelMap;
import io.sparkled.model.render.RenderedFrame;
import io.sparkled.music.SongPlayerServiceImpl;

import javax.inject.Inject;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class RequestHandlerImpl implements RequestHandler {

    private static final String GET_FRAME_COMMAND = "GF";

    private final SongPlayerServiceImpl songPlayerService;

    @Inject
    public RequestHandlerImpl(SongPlayerServiceImpl songPlayerService) {
        this.songPlayerService = songPlayerService;
    }

    @Override
    public void handle(DatagramSocket serverSocket, DatagramPacket receivePacket) throws IOException {
        String message = new String(receivePacket.getData()).substring(0, receivePacket.getLength());
        String[] components = message.split(":");

        if (components.length == 2 && GET_FRAME_COMMAND.equals(components[0])) {
            String controller = components[1];
            double progress = songPlayerService.getSongProgress();
            Song currentSong = songPlayerService.getCurrentSong();
            RenderedChannelMap renderedChannelMap = songPlayerService.getRenderedChannelMap();

            if (currentSong == null || renderedChannelMap == null) {
                respond(serverSocket, receivePacket, "ERR".getBytes(StandardCharsets.US_ASCII));
            } else {
                final int durationFrames = currentSong.getDurationFrames();
                final int frameIndex = (int) Math.min(durationFrames - 1, Math.round(progress * durationFrames));

                RenderedFrame renderedFrame = renderedChannelMap.get(controller).getFrames().get(frameIndex);
                respond(serverSocket, receivePacket, renderedFrame.getData());
            }
        } else {
            respond(serverSocket, receivePacket, "ERR".getBytes(StandardCharsets.US_ASCII));
        }
    }

    private void respond(DatagramSocket serverSocket, DatagramPacket receivePacket, byte[] data) throws IOException {
        InetAddress IPAddress = receivePacket.getAddress();
        int port = receivePacket.getPort();
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, port);
        serverSocket.send(sendPacket);
    }
}
