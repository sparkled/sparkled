package io.sparkled.model.validator;

import io.sparkled.model.entity.Stage;
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
    }
}
