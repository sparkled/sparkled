package io.sparkled.udpserver.impl.command;

import io.sparkled.model.setting.SettingsCache;
import io.sparkled.music.PlaybackState;
import io.sparkled.udpserver.impl.RequestHandlerImpl;

public abstract class RequestCommand {

    public abstract byte[] getResponse(String[] args, SettingsCache settings, PlaybackState playbackState);

    byte[] getErrorResponse() {
        return RequestHandlerImpl.ERROR_CODE_BYTES;
    }
}
