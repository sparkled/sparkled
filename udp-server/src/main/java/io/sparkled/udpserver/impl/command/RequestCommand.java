package io.sparkled.udpserver.impl.command;

import io.sparkled.music.PlaybackState;
import io.sparkled.udpserver.impl.RequestHandlerImpl;

public abstract class RequestCommand {

    public abstract byte[] getResponse(String[] args, PlaybackState playbackState);

    byte[] getErrorResponse() {
        return RequestHandlerImpl.ERROR_CODE_BYTES;
    }
}
