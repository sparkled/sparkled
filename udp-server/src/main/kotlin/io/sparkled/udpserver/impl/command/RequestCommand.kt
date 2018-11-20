package io.sparkled.udpserver.impl.command

import io.sparkled.model.setting.SettingsCache
import io.sparkled.music.PlaybackState

abstract class RequestCommand {

    abstract fun getResponse(args: List<String>, settings: SettingsCache, playbackState: PlaybackState): ByteArray

    internal val emptyResponse: ByteArray = ByteArray(0)
}
