package io.sparkled.model.validator.exception

/**
 * Thrown when an entity lookup yields no result when there should definitely be one. The message contained in the
 * exception is guaranteed to be user friendly.
 */
class EntityNotFoundException : RuntimeException {

    constructor(message: String) : super(message) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}
}
