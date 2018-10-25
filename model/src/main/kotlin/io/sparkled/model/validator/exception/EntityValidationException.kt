package io.sparkled.model.validator.exception

/**
 * Thrown when an entity validation failure occurs. The message contained in the exception is
 * guaranteed to be user friendly.
 */
class EntityValidationException : RuntimeException {

    constructor(message: String) : super(message) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}
}
