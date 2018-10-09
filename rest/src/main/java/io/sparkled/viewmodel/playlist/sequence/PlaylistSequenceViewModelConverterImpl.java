package io.sparkled.viewmodel.playlist.sequence;

import io.sparkled.model.entity.PlaylistSequence;
import io.sparkled.persistence.playlist.PlaylistPersistenceService;

import javax.inject.Inject;

public class PlaylistSequenceViewModelConverterImpl extends PlaylistSequenceViewModelConverter {

    private PlaylistPersistenceService playlistPersistenceService;

    @Inject
    public PlaylistSequenceViewModelConverterImpl(PlaylistPersistenceService playlistPersistenceService) {
        this.playlistPersistenceService = playlistPersistenceService;
    }

    @Override
    public PlaylistSequenceViewModel toViewModel(PlaylistSequence model) {
        return new PlaylistSequenceViewModel()
                .setUuid(model.getUuid())
                .setSequenceId(model.getSequenceId())
                .setDisplayOrder(model.getDisplayOrder());
    }

    @Override
    public PlaylistSequence toModel(PlaylistSequenceViewModel viewModel) {
        PlaylistSequence model = playlistPersistenceService.getPlaylistSequenceByUuid(viewModel.getSequenceId(), viewModel.getUuid())
                .orElseGet(PlaylistSequence::new);

        return model
                .setUuid(viewModel.getUuid())
                .setSequenceId(viewModel.getSequenceId())
                .setDisplayOrder(viewModel.getDisplayOrder());
    }
}
