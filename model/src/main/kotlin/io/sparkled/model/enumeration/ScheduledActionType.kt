package io.sparkled.model.enumeration

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue

enum class ScheduledActionType {
    @JsonEnumDefaultValue
    NONE,
    PLAY_PLAYLIST,
    STOP_PLAYBACK,
    SET_BRIGHTNESS
}
