package io.sparkled.model.validator;

import io.sparkled.model.entity.StageProp;
import io.sparkled.model.validator.exception.EntityValidationException;

import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class StagePropValidator {

    public void validate(List<StageProp> stageProps) {
        checkForDuplicates(stageProps);
        stageProps.forEach(this::validateStageProp);
    }

    private void checkForDuplicates(List<StageProp> stageProps) {
        List<String> stagePropCodes = stageProps.stream().map(StageProp::getCode).collect(toList());
        Set<String> duplicateCodes = ValidatorUtils.findDuplicates(stagePropCodes);

        if (!duplicateCodes.isEmpty()) {
            String duplicateCodeList = duplicateCodes.stream().collect(joining(", "));
            throw new EntityValidationException(String.format(Errors.DUPLICATES_FOUND, duplicateCodeList));
        }
    }

    private void validateStageProp(StageProp stageProp) {
        if (stageProp.getUuid() == null) {
            throw new EntityValidationException(Errors.UUID_MISSING);
        } else if (stageProp.getStageId() == null) {
            throw new EntityValidationException(Errors.STAGE_ID_MISSING);
        } else if (stageProp.getCode() == null) {
            throw new EntityValidationException(Errors.CODE_MISSING);
        } else if (stageProp.getName() == null) {
            throw new EntityValidationException(Errors.NAME_MISSING);
        } else if (stageProp.getType() == null) {
            throw new EntityValidationException(Errors.TYPE_MISSING);
        } else if (stageProp.getLedCount() == null || stageProp.getLedCount() < 0) {
            throw new EntityValidationException(Errors.LED_COUNT_NEGATIVE);
        } else if (stageProp.getPositionX() == null) {
            throw new EntityValidationException(Errors.POSITION_X_MISSING);
        } else if (stageProp.getPositionY() == null) {
            throw new EntityValidationException(Errors.POSITION_Y_MISSING);
        } else if (stageProp.getPositionX() == null) {
            throw new EntityValidationException(Errors.SCALE_X_MISSING);
        } else if (stageProp.getPositionY() == null) {
            throw new EntityValidationException(Errors.SCALE_Y_MISSING);
        } else if (stageProp.getRotation() == null || stageProp.getRotation() < -180 || stageProp.getRotation() > 180) {
            throw new EntityValidationException(Errors.ROTATION_INVALID);
        } else if (stageProp.getDisplayOrder() == null) {
            throw new EntityValidationException(Errors.DISPLAY_ORDER_MISSING);
        }
    }

    private static class Errors {
        static final String DUPLICATES_FOUND = "Duplicate stage prop code(s) (%s) are not allowed.";
        static final String UUID_MISSING = "Stage prop has no unique identifier.";
        static final String STAGE_ID_MISSING = "Stage prop has no stage identifier.";
        static final String CODE_MISSING = "Stage prop code must not be empty.";
        static final String NAME_MISSING = "Stage prop name must not be empty.";
        static final String TYPE_MISSING = "Stage prop type must not be empty.";
        static final String LED_COUNT_NEGATIVE = "Stage prop LED count must be non-negative.";
        static final String POSITION_X_MISSING = "Stage prop X position must not be empty.";
        static final String POSITION_Y_MISSING = "Stage prop X position must not be empty.";
        static final String SCALE_X_MISSING = "Stage prop X scale must not be empty.";
        static final String SCALE_Y_MISSING = "Stage prop X scale must not be empty.";
        static final String ROTATION_INVALID = "Stage prop rotation must be between -180 and 180 degrees.";
        static final String DISPLAY_ORDER_MISSING = "Stage prop X display order must not be empty.";
    }
}
