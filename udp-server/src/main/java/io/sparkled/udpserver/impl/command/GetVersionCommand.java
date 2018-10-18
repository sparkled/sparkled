package io.sparkled.udpserver.impl.command;

import io.sparkled.music.PlaybackState;

/**
 * Retrieves the current UDP protocol version. This can be used by clients to determine whether they can support the
 * protocol of the Sparkled instance they are talking to.
 * Command syntax: GV
 */
public class GetVersionCommand extends RequestCommand {

    public static final String KEY = "GV";

    @Override
    public byte[] getResponse(String[] args, PlaybackState playbackState) {
        return new byte[]{1};
    }
}
