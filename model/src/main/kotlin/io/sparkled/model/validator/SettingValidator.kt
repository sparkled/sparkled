package io.sparkled.model.validator

import io.sparkled.model.SettingModel
import io.sparkled.model.setting.SettingsConstants
import io.sparkled.model.validator.exception.EntityValidationException

class SettingValidator {

    fun validate(setting: SettingModel) {
        val code = setting.code
        val value = setting.value
        val isBrightness = code === SettingsConstants.Brightness.CODE

        when {
            isBrightness && value.toIntOrNull() == null -> throw EntityValidationException(Errors.VALUE_INVALID)
        }
    }

    private object Errors {
        const val VALUE_INVALID = "Brightness must be between 0 and 100 inclusive."
    }
}
