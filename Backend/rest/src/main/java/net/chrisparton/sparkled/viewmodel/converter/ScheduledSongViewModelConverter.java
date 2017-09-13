package net.chrisparton.sparkled.viewmodel.converter;

import net.chrisparton.sparkled.entity.ScheduledSong;
import net.chrisparton.sparkled.persistence.song.SongPersistenceService;
import net.chrisparton.sparkled.viewmodel.ScheduledSongViewModel;

import java.util.Optional;

public class ScheduledSongViewModelConverter implements ViewModelConverter<ScheduledSongViewModel, ScheduledSong> {

    private SongPersistenceService songPersistenceService = new SongPersistenceService();

    @Override
    public ScheduledSongViewModel toViewModel(ScheduledSong model) {
        ScheduledSongViewModel viewModel = new ScheduledSongViewModel();
        viewModel.setId(model.getId());
        viewModel.setStartTime(model.getStartTime());
        viewModel.setEndTime(model.getEndTime());

        Optional.of(model.getSong())
                .ifPresent(song -> viewModel.setSongId(song.getId()));

        return viewModel;
    }

    @Override
    public ScheduledSong fromViewModel(ScheduledSongViewModel viewModel) {
        ScheduledSong model = new ScheduledSong();
        model.setId(viewModel.getId());
        model.setStartTime(viewModel.getStartTime());
        model.setEndTime(viewModel.getEndTime());

        songPersistenceService.getSongById(viewModel.getSongId())
                .ifPresent(model::setSong);

        return model;
    }
}
