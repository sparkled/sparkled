package io.sparkled.viewmodel.converter;

import io.sparkled.model.entity.ScheduledSong;
import io.sparkled.viewmodel.ScheduledSongViewModel;

public interface ScheduledSongViewModelConverter extends ViewModelConverter<ScheduledSongViewModel, ScheduledSong> {

    @Override
    ScheduledSongViewModel toViewModel(ScheduledSong model);

    @Override
    ScheduledSong fromViewModel(ScheduledSongViewModel viewModel);
}
