package io.sparkled.udpserver.impl.command

import io.sparkled.model.setting.SettingsCache
import io.sparkled.music.PlaybackState
import io.sparkled.udpserver.impl.RequestHandlerImpl

abstract class RequestCommand {

    abstract fun getResponse(args: List<String>, settings: SettingsCache, playbackState: PlaybackState): ByteArray

    internal val errorResponse: ByteArray
        get() = RequestHandlerImpl.ERROR_CODE_BYTES
}
