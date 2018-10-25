package io.sparkled.model.validator

import io.sparkled.model.entity.Sequence
import io.sparkled.model.validator.exception.EntityValidationException

class SequenceValidator {

    fun validate(sequence: Sequence) {
        if (sequence.getName() == null) {
            throw EntityValidationException(Errors.NAME_MISSING)
        } else if (sequence.getFramesPerSecond() <= 0) {
            throw EntityValidationException(Errors.FRAMES_PER_SECOND_NOT_POSITIVE)
        }
    }

    private object Errors {
        internal val NAME_MISSING = "Sequence name must not be empty."
        internal val FRAMES_PER_SECOND_NOT_POSITIVE = "Sequence frames per second must be greater than 0."
    }
}
