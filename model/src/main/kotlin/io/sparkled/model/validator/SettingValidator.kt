package io.sparkled.model.validator

import io.sparkled.model.entity.Setting
import io.sparkled.model.validator.exception.EntityValidationException

class SettingValidator {

    fun validate(setting: Setting) {
        if (setting.getCode() == null) {
            throw EntityValidationException(Errors.CODE_MISSING)
        }
    }

    private object Errors {
        internal const val CODE_MISSING = "Setting code must not be empty."
    }
}
