package io.sparkled.model.setting

object SettingsConstants {

    object Brightness {
        const val CODE = "BRIGHTNESS"
        const val MIN = 0
        const val MAX = 255
    }

    object FramesPerSecond {
        const val CODE = "FRAMES_PER_SECOND"
        const val DEFAULT = 60
        val VALUES = listOf(15, 30, 45, 60)
    }
}
