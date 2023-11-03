package io.sparkled.model.validator

import io.sparkled.model.SequenceModel
import io.sparkled.model.validator.exception.EntityValidationException

class SequenceValidator {

    fun validate(sequence: SequenceModel) {
        if (sequence.framesPerSecond <= 0) {
            throw EntityValidationException(Errors.FRAMES_PER_SECOND_NOT_POSITIVE)
        }
    }

    private object Errors {
        const val FRAMES_PER_SECOND_NOT_POSITIVE = "Sequence frames per second must be greater than 0."
    }
}
