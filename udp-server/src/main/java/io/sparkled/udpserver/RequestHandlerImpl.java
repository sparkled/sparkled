package io.sparkled.udpserver;

import io.sparkled.model.entity.Sequence;
import io.sparkled.model.entity.Song;
import io.sparkled.model.render.RenderedFrame;
import io.sparkled.model.render.RenderedStagePropData;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.model.util.SequenceUtils;
import io.sparkled.music.PlaylistServiceImpl;

import javax.inject.Inject;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

public class RequestHandlerImpl implements RequestHandler {

    private static final String GET_FRAME_COMMAND = "GF";
    private static final byte[] ERROR_CODE_BYTES = "ERR".getBytes(StandardCharsets.US_ASCII);

    private final PlaylistServiceImpl sequencePlayerService;

    @Inject
    public RequestHandlerImpl(PlaylistServiceImpl sequencePlayerService) {
        this.sequencePlayerService = sequencePlayerService;
    }

    @Override
    public void handle(DatagramSocket serverSocket, DatagramPacket receivePacket) throws IOException {
        String message = new String(receivePacket.getData()).substring(0, receivePacket.getLength());
        String[] components = message.split(":");

        if (components.length == 2 && GET_FRAME_COMMAND.equals(components[0])) {
            String stagePropCode = components[1];
            double progress = sequencePlayerService.getSequenceProgress();
            Song currentSong = sequencePlayerService.getCurrentSong();
            Sequence currentSequence = sequencePlayerService.getCurrentSequence();
            RenderedStagePropDataMap renderedStagePropDataMap = sequencePlayerService.getRenderedStageProps();

            if (currentSequence == null || currentSong == null || renderedStagePropDataMap == null) {
                sendErrorResponse(serverSocket, receivePacket);
            } else {
                final int frameCount = SequenceUtils.getFrameCount(currentSong, currentSequence);
                final int frameIndex = (int) Math.min(frameCount - 1, Math.round(progress * frameCount));

                RenderedFrame renderedFrame = getRenderedFrame(stagePropCode, frameIndex);
                if (renderedFrame == null) {
                    sendErrorResponse(serverSocket, receivePacket);
                } else {
                    respond(serverSocket, receivePacket, renderedFrame.getData());
                }
            }
        } else {
            sendErrorResponse(serverSocket, receivePacket);
        }
    }

    private void sendErrorResponse(DatagramSocket serverSocket, DatagramPacket receivePacket) throws IOException {
        respond(serverSocket, receivePacket, ERROR_CODE_BYTES);
    }

    private RenderedFrame getRenderedFrame(String stagePropCode, int frameIndex) {
        RenderedStagePropDataMap renderedStagePropDataMap = sequencePlayerService.getRenderedStageProps();
        UUID stagePropUuid = sequencePlayerService.getStagePropUuid(stagePropCode);
        RenderedStagePropData renderedStagePropData = renderedStagePropDataMap.get(stagePropUuid);
        RenderedFrame frame = null;

        if (renderedStagePropData != null) {
            List<RenderedFrame> frames = renderedStagePropData.getFrames();
            frame = frames == null ? null : frames.get(frameIndex);
        }

        return frame;
    }

    private void respond(DatagramSocket serverSocket, DatagramPacket receivePacket, byte[] data) throws IOException {
        InetAddress IPAddress = receivePacket.getAddress();
        int port = receivePacket.getPort();
        DatagramPacket sendPacket = new DatagramPacket(data, data.length, IPAddress, port);
        serverSocket.send(sendPacket);
    }
}
