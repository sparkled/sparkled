package io.sparkled.viewmodel;

import java.util.Collection;
import java.util.List;

/**
 * Handles model -> view model collection conversion.
 *
 * @param <M>  A model class
 * @param <VM> A view model class
 */
public interface ModelCollectionConverter<M, VM extends ViewModel> {

    /**
     * @param models The models to be converted
     * @return The converted view models.
     */
    List<VM> toViewModels(Collection<M> models);
}
