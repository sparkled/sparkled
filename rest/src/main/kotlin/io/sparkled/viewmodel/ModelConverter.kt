package io.sparkled.viewmodel

/**
 * Handles model -> view model conversion.

 * @param <M>  A model class
 * @param <VM> A view model class
 */
interface ModelConverter<M, VM : ViewModel> {

    /**
     * @param model The model to be converted
     * @return The converted view model.
     */
    fun toViewModel(model: M): VM
}
