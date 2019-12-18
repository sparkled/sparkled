package io.sparkled.model.entity

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue

enum class ScheduledJobAction {
    @JsonEnumDefaultValue
    NONE,
    PLAY_PLAYLIST,
    STOP_PLAYBACK,
    SET_BRIGHTNESS
}
