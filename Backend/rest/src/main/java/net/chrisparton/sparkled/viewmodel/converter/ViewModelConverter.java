package net.chrisparton.sparkled.viewmodel.converter;

/**
 * Handles bidirectional conversion between view models and models.
 * @param <VM> A ViewModel class
 * @param <M> A Model class
 */
public interface ViewModelConverter<VM, M> {

    /**
     * @param model The model class to be converted
     * @return The converted view model.
     */
    VM toViewModel(M model);

    /**
     * @param viewModel The view model class to be converted
     * @return The converted model.
     */
    M fromViewModel(VM viewModel);
}
