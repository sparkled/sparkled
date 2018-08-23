package io.sparkled.model.validator;

import io.sparkled.model.entity.Stage;
import io.sparkled.model.entity.StageProp;
import io.sparkled.model.validator.exception.EntityValidationException;

public class StageValidator {

    private Stage stage;

    public StageValidator(Stage stage) {
        this.stage = stage;
    }

    public void validate() {
        if (stage.getName() == null) {
            throw new EntityValidationException("Stage name must not be empty.");
        }

        stage.getStageProps().forEach(this::validateStageProp);
    }

    private void validateStageProp(StageProp stageProp) {
        // TODO: Add more validations.

        if (stageProp.getCode() == null) {
            throw new EntityValidationException("Stage prop code must not be empty.");
        } else if (stageProp.getLeds() < 0) {
            throw new EntityValidationException("Stage prop LED count must be non-negative.");
        }
    }
}
