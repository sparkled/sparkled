package io.sparkled.viewmodel;

/**
 * Handles view model -> model conversion.
 *
 * @param <VM> A view model class
 * @param <M>  A model class
 */
public interface ViewModelConverter<VM extends ViewModel, M> {

    /**
     * @param viewModel The view model to be converted
     * @return The converted model.
     */
    M toModel(VM viewModel);
}
