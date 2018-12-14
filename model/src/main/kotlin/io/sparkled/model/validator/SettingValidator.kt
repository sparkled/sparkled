package io.sparkled.model.validator

import io.sparkled.model.entity.Setting
import io.sparkled.model.setting.SettingsConstants
import io.sparkled.model.validator.exception.EntityValidationException

class SettingValidator {

    fun validate(setting: Setting) {
        val code = setting.getCode()
        val value = setting.getValue()
        val isBrightness = code === SettingsConstants.Brightness.CODE

        when {
            code == null -> throw EntityValidationException(Errors.CODE_MISSING)
            value == null -> throw EntityValidationException(Errors.VALUE_MISSING)
            isBrightness && value.toIntOrNull() == null -> throw EntityValidationException(Errors.VALUE_INVALID)
        }
    }

    private object Errors {
        internal const val CODE_MISSING = "Setting code must not be empty."
        internal const val VALUE_MISSING = "Setting value must not be empty."
        internal const val VALUE_INVALID = "Setting value is invalid."
    }
}
