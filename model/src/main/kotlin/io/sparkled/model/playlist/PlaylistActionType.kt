package io.sparkled.model.playlist

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue

enum class PlaylistActionType {
    @JsonEnumDefaultValue
    NONE,
    PLAY_PLAYLIST,
    PLAY_SEQUENCE,
    STOP
}
