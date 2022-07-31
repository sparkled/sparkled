package io.sparkled.viewmodel.exception

/**
 * Thrown when a view model conversion failure occurs. The message contained in the exception is
 * guaranteed to be user friendly.
 */
class ViewModelConversionException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)
