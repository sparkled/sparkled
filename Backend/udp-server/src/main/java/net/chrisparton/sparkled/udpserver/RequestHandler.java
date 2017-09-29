package net.chrisparton.sparkled.udpserver;

import net.chrisparton.sparkled.entity.Song;
import net.chrisparton.sparkled.music.SongPlayerService;
import net.chrisparton.sparkled.music.SongSchedulerService;
import net.chrisparton.sparkled.renderdata.RenderedChannelMap;
import net.chrisparton.sparkled.renderdata.RenderedFrame;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

class RequestHandler {
    private static final String GET_FRAME_COMMAND = "GF";
    private static final SongPlayerService songPlayerService = SongSchedulerService.instance().getSongPlayerService();

    void handle(DatagramSocket serverSocket, DatagramPacket receivePacket) throws IOException {
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
                int frameIndex = (int) Math.round(progress * currentSong.getDurationFrames());
                RenderedFrame renderedFrame = renderedChannelMap.get(controller).getFrames().get(frameIndex);
                respond(serverSocket, receivePacket, renderedFrame.getLedData());
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
