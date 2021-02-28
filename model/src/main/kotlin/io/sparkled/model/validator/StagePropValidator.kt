package io.sparkled.model.validator

import io.sparkled.model.entity.v2.StagePropEntity
import io.sparkled.model.validator.exception.EntityValidationException

class StagePropValidator {

    fun validate(stageProps: List<StagePropEntity>) {
        checkForDuplicates(stageProps)
        stageProps.forEach(this::validateStageProp)
    }

    private fun checkForDuplicates(stageProps: List<StagePropEntity>) {
        val stagePropCodes = stageProps.map(StagePropEntity::code)
        val duplicateCodes = ValidatorUtils.findDuplicates(stagePropCodes)

        if (duplicateCodes.isNotEmpty()) {
            val duplicateCodeList = duplicateCodes.joinToString(", ")
            throw EntityValidationException(String.format(Errors.DUPLICATES_FOUND, duplicateCodeList))
        }
    }

    private fun validateStageProp(stageProp: StagePropEntity) {
        if (stageProp.ledCount < 0) {
            throw EntityValidationException(Errors.LED_COUNT_NEGATIVE)
        } else if (stageProp.rotation < 0 || stageProp.rotation > 360) {
            throw EntityValidationException(Errors.ROTATION_INVALID)
        } else if (stageProp.brightness < 0 || stageProp.brightness > 100) {
            throw EntityValidationException(Errors.BRIGHTNESS_INVALID)
        }
    }

    private object Errors {
        const val DUPLICATES_FOUND = "Duplicate stage prop code(s) (%s) are not allowed."
        const val LED_COUNT_NEGATIVE = "Stage prop LED count must be non-negative."
        const val ROTATION_INVALID = "Stage prop rotation must be between 0 and 360 degrees."
        const val BRIGHTNESS_INVALID = "Stage prop brightness must be between 0% and 100%."
    }
}
