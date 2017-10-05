package net.chrisparton.sparkled.viewmodel.converter;

import net.chrisparton.sparkled.entity.ScheduledSong;
import net.chrisparton.sparkled.viewmodel.ScheduledSongViewModel;

public interface ScheduledSongViewModelConverter extends ViewModelConverter<ScheduledSongViewModel, ScheduledSong> {

    @Override
    ScheduledSongViewModel toViewModel(ScheduledSong model);

    @Override
    ScheduledSong fromViewModel(ScheduledSongViewModel viewModel);
}
