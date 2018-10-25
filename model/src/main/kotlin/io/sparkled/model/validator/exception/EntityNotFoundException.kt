package io.sparkled.model.validator.exception;

/**
 * Thrown when an entity lookup yields no result when there should definitely be one. The message contained in the
 * exception is guaranteed to be user friendly.
 */
public class EntityNotFoundException extends RuntimeException {

    public EntityNotFoundException(String message) {
        super(message);
    }

    public EntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
