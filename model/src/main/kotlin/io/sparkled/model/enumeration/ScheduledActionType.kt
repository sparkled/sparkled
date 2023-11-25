package io.sparkled.model.enumeration

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue
import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
enum class ScheduledActionType {
    @JsonEnumDefaultValue
    NONE,
    PLAY_PLAYLIST,
    STOP_PLAYBACK,
    SET_BRIGHTNESS
}
