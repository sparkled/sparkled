package io.sparkled.viewmodel.exception;

/**
 * Thrown when a view model conversion failure occurs. The message contained in the exception is
 * guaranteed to be user friendly.
 */
public class ViewModelConversionException extends RuntimeException {

    public ViewModelConversionException(String message) {
        super(message);
    }

    public ViewModelConversionException(String message, Throwable cause) {
        super(message, cause);
    }
}
