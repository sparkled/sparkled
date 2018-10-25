package io.sparkled.viewmodel

/**
 * Handles view model -> model conversion.

 * @param <VM> A view model class
 * *
 * @param <M>  A model class
</M></VM> */
interface ViewModelConverter<VM : ViewModel, M> {

    /**
     * @param viewModel The view model to be converted
     * *
     * @return The converted model.
     */
    fun toModel(viewModel: VM): M
}
