package io.sparkled.viewmodel.playlist.sequence

import io.sparkled.model.entity.PlaylistSequence
import io.sparkled.model.util.IdUtils.NO_ID
import io.sparkled.model.util.IdUtils.NO_UUID
import io.sparkled.persistence.playlist.PlaylistPersistenceService
import javax.inject.Inject

class PlaylistSequenceViewModelConverterImpl
@Inject constructor(private val playlistPersistenceService: PlaylistPersistenceService) : PlaylistSequenceViewModelConverter() {

    override fun toViewModel(model: PlaylistSequence): PlaylistSequenceViewModel {
        return PlaylistSequenceViewModel()
            .setUuid(model.getUuid())
            .setSequenceId(model.getSequenceId())
            .setDisplayOrder(model.getDisplayOrder())
    }

    override fun toModel(viewModel: PlaylistSequenceViewModel): PlaylistSequence {
        val model = playlistPersistenceService
            .getPlaylistSequenceByUuid(viewModel.getSequenceId() ?: NO_ID, viewModel.getUuid() ?: NO_UUID)
            ?: PlaylistSequence()

        return model
            .setUuid(viewModel.getUuid())
            .setSequenceId(viewModel.getSequenceId())
            .setDisplayOrder(viewModel.getDisplayOrder())
    }
}
