package io.sparkled.model.validator.exception;

/**
 * Thrown when an entity validation failure occurs. The message contained in the exception is
 * guaranteed to be user friendly.
 */
public class EntityValidationException extends RuntimeException {

    public EntityValidationException(String message) {
        super(message);
    }

    public EntityValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
