package io.sparkled.model.validator;

import io.sparkled.model.entity.Stage;
import io.sparkled.model.validator.exception.EntityValidationException;

public class StageValidator {

    public void validate(Stage stage) {
        if (stage.getName() == null) {
            throw new EntityValidationException(Errors.NAME_MISSING);
        }
    }

    private static class Errors {
        static final String NAME_MISSING = "Stage name must not be empty.";
    }
}
