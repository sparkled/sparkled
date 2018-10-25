package io.sparkled.viewmodel.playlist.search

import io.sparkled.model.entity.Playlist
import io.sparkled.model.playlist.PlaylistSummary
import io.sparkled.persistence.playlist.PlaylistPersistenceService

import javax.inject.Inject

import java.util.stream.Collectors.toList

class PlaylistSearchViewModelConverterImpl @Inject
constructor(private val playlistPersistenceService: PlaylistPersistenceService) : PlaylistSearchViewModelConverter() {

    @Override
    fun toViewModels(models: Collection<Playlist>): List<PlaylistSearchViewModel> {
        val playlistSummaries = playlistPersistenceService.getPlaylistSummaries()

        return models.stream()
                .map({ model -> toViewModel(model, playlistSummaries) })
                .collect(toList())
    }

    private fun toViewModel(model: Playlist, playlistSummaries: Map<Integer, PlaylistSummary>): PlaylistSearchViewModel {
        val summary = playlistSummaries[model.getId()]

        return PlaylistSearchViewModel()
                .setId(model.getId())
                .setName(model.getName())
                .setSequenceCount(summary.getSequenceCount())
                .setDurationSeconds(summary.getDurationSeconds())
    }
}
