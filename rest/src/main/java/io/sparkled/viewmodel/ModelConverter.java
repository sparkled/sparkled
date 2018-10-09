package io.sparkled.viewmodel;

/**
 * Handles model -> view model conversion.
 *
 * @param <M>  A model class
 * @param <VM> A view model class
 */
public interface ModelConverter<M, VM extends ViewModel> {

    /**
     * @param model The model class to be converted
     * @return The converted view model.
     */
    VM toViewModel(M model);
}
