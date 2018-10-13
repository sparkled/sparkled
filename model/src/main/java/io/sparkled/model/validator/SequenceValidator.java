package io.sparkled.model.validator;

import io.sparkled.model.entity.Sequence;
import io.sparkled.model.validator.exception.EntityValidationException;

public class SequenceValidator {

    public void validate(Sequence sequence) {
        if (sequence.getName() == null) {
            throw new EntityValidationException(Errors.NAME_MISSING);
        } else if (sequence.getFramesPerSecond() <= 0) {
            throw new EntityValidationException(Errors.FRAMES_PER_SECOND_NOT_POSITIVE);
        }
    }

    private static class Errors {
        static final String NAME_MISSING = "Sequence name must not be empty.";
        static final String FRAMES_PER_SECOND_NOT_POSITIVE = "Sequence frames per second must be greater than 0.";
    }
}
