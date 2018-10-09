package io.sparkled.viewmodel;

/**
 * Handles view model -> model conversion.
 *
 * @param <VM> A ViewModel class
 * @param <M>  A Model class
 */
public interface ViewModelConverter<VM extends ViewModel, M> {

    /**
     * @param viewModel The view model class to be converted
     * @return The converted model.
     */
    M toModel(VM viewModel);
}
