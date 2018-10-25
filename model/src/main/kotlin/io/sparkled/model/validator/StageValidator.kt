package io.sparkled.model.validator

import io.sparkled.model.entity.Stage
import io.sparkled.model.validator.exception.EntityValidationException

class StageValidator {

    fun validate(stage: Stage) {
        if (stage.getName() == null) {
            throw EntityValidationException(Errors.NAME_MISSING)
        }
    }

    private object Errors {
        internal const val NAME_MISSING = "Stage name must not be empty."
    }
}
