package io.sparkled.viewmodel.exception

/**
 * Thrown when a view model conversion failure occurs. The message contained in the exception is
 * guaranteed to be user friendly.
 */
class ViewModelConversionException : RuntimeException {

    constructor(message: String) : super(message) {}

    constructor(message: String, cause: Throwable) : super(message, cause) {}
}
