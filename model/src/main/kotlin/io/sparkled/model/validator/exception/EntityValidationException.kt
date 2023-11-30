package io.sparkled.model.validator.exception

/**
 * Thrown when an entity validation failure occurs. The message contained in the exception is
 * guaranteed to be user-friendly.
 */
class EntityValidationException(message: String) : RuntimeException(message)
