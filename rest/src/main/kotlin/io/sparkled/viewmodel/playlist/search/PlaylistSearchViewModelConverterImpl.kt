package io.sparkled.viewmodel.playlist.search

import io.sparkled.model.entity.Playlist
import io.sparkled.model.playlist.PlaylistSummary
import io.sparkled.persistence.playlist.PlaylistPersistenceService
import javax.inject.Inject

class PlaylistSearchViewModelConverterImpl @Inject
constructor(private val playlistPersistenceService: PlaylistPersistenceService) : PlaylistSearchViewModelConverter() {

    override fun toViewModels(models: Collection<Playlist>): List<PlaylistSearchViewModel> {
        val playlistSummaries = playlistPersistenceService.getPlaylistSummaries()
        return models.map { model -> toViewModel(model, playlistSummaries) }.toList()
    }

    private fun toViewModel(model: Playlist, playlistSummaries: Map<Int, PlaylistSummary>): PlaylistSearchViewModel {
        val summary = playlistSummaries[model.getId()]!!

        return PlaylistSearchViewModel()
                .setId(model.getId())
                .setName(model.getName())
                .setSequenceCount(summary.getSequenceCount())
                .setDurationSeconds(summary.getDurationSeconds())
    }
}
