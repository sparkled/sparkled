package io.sparkled.udpserver.impl.command;

import io.sparkled.model.setting.SettingsCache;
import io.sparkled.music.PlaybackState;

/**
 * Retrieves the current UDP protocol version. This can be used by clients to determine whether they can support the
 * protocol of the Sparkled instance they are talking to.
 * Command syntax: GV
 */
public class GetVersionCommand extends RequestCommand {

    public static final String KEY = "GV";
    private static final int UDP_PROTOCOL_VERSION = 1;

    @Override
    public byte[] getResponse(String[] args, SettingsCache settings, PlaybackState playbackState) {
        return new byte[]{UDP_PROTOCOL_VERSION};
    }
}
