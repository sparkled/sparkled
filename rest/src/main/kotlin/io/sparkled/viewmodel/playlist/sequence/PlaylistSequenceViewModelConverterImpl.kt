package io.sparkled.viewmodel.playlist.sequence

import io.sparkled.model.entity.PlaylistSequence
import io.sparkled.persistence.playlist.PlaylistPersistenceService

import javax.inject.Inject

class PlaylistSequenceViewModelConverterImpl @Inject
constructor(private val playlistPersistenceService: PlaylistPersistenceService) : PlaylistSequenceViewModelConverter() {

    @Override
    fun toViewModel(model: PlaylistSequence): PlaylistSequenceViewModel {
        return PlaylistSequenceViewModel()
                .setUuid(model.getUuid())
                .setSequenceId(model.getSequenceId())
                .setDisplayOrder(model.getDisplayOrder())
    }

    @Override
    fun toModel(viewModel: PlaylistSequenceViewModel): PlaylistSequence {
        val model = playlistPersistenceService.getPlaylistSequenceByUuid(viewModel.getSequenceId(), viewModel.getUuid())
                .orElseGet(???({ PlaylistSequence() }))

        return model
                .setUuid(viewModel.getUuid())
                .setSequenceId(viewModel.getSequenceId())
                .setDisplayOrder(viewModel.getDisplayOrder())
    }
}
