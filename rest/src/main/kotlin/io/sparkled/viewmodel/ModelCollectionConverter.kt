package io.sparkled.viewmodel

/**
 * Handles model -> view model collection conversion.
 * @param <M>  A model class
 * @param <VM> A view model class
 */
interface ModelCollectionConverter<M, VM : ViewModel> {

    /**
     * @param models The models to be converted
     * @return The converted view models.
     */
    fun toViewModels(models: Collection<M>): List<VM>
}
