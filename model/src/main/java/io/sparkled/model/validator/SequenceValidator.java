package io.sparkled.model.validator;

import io.sparkled.model.entity.Sequence;
import io.sparkled.model.validator.exception.EntityValidationException;

public class SequenceValidator {

    public void validate(Sequence sequence) {
        if (sequence.getName() == null) {
            throw new EntityValidationException(Errors.NAME_MISSING);
        }
    }

    private static class Errors {
        static final String NAME_MISSING = "Sequence name must not be empty.";
    }
}
