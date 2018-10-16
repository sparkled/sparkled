package io.sparkled.udpserver;

import io.sparkled.model.render.RenderedFrame;
import io.sparkled.model.render.RenderedStagePropData;
import io.sparkled.model.render.RenderedStagePropDataMap;
import io.sparkled.model.util.SequenceUtils;
import io.sparkled.music.PlaybackState;
import io.sparkled.music.PlaybackStateService;

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

    private final PlaybackStateService playbackStateService;

    @Inject
    public RequestHandlerImpl(PlaybackStateService playbackStateService) {
        this.playbackStateService = playbackStateService;
    }

    @Override
    public void handle(DatagramSocket serverSocket, DatagramPacket receivePacket) throws IOException {
        String message = new String(receivePacket.getData()).substring(0, receivePacket.getLength());
        String[] components = message.split(":");

        if (components.length == 2 && GET_FRAME_COMMAND.equals(components[0])) {
            String stagePropCode = components[1];

            PlaybackState playbackState = playbackStateService.getPlaybackState();
            if (playbackState.isEmpty()) {
                sendErrorResponse(serverSocket, receivePacket);
            } else {
                final int frameCount = SequenceUtils.getFrameCount(playbackState.getSong(), playbackState.getSequence());
                final int frameIndex = (int) Math.min(frameCount - 1, Math.round(playbackState.getProgress() * frameCount));

                RenderedFrame renderedFrame = getRenderedFrame(playbackState, stagePropCode, frameIndex);
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

    private RenderedFrame getRenderedFrame(PlaybackState playbackState, String stagePropCode, int frameIndex) {
        RenderedStagePropDataMap renderedStagePropDataMap = playbackState.getRenderedStageProps();
        UUID stagePropUuid = playbackState.getStagePropUuids().get(stagePropCode);
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
