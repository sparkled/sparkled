package io.sparkled.viewmodel

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
enum class PlaylistActionType {
    @JsonEnumDefaultValue
    NONE,
    PLAY_PLAYLIST,
    PLAY_SEQUENCE,
    STOP
}
