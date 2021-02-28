package io.sparkled.model.validator

import io.sparkled.model.entity.ScheduledJobAction
import io.sparkled.model.entity.v2.ScheduledTaskEntity
import io.sparkled.model.validator.exception.EntityValidationException

class ScheduledTaskValidator {

    fun validate(scheduledTask: ScheduledTaskEntity) {
        when {
            scheduledTask.action == ScheduledJobAction.PLAY_PLAYLIST && scheduledTask.playlistId == null -> throw EntityValidationException(Errors.NO_PLAYLIST_SPECIFIED)
            scheduledTask.action == ScheduledJobAction.SET_BRIGHTNESS && scheduledTask.value.isNullOrBlank() -> throw EntityValidationException(Errors.NO_BRIGHTNESS_SPECIFIED)
            !scheduledTask.value.isNullOrBlank() -> throw EntityValidationException(Errors.UNEXPECTED_VALUE)
        }
    }

    private object Errors {
        const val NO_PLAYLIST_SPECIFIED = "A playlist must be specified."
        const val NO_BRIGHTNESS_SPECIFIED = "A brightness must be specified."
        const val UNEXPECTED_VALUE = "A value was specified where none was expected."
    }
}
