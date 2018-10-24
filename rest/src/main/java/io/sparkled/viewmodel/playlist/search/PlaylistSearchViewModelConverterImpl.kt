package io.sparkled.viewmodel.playlist.search;

import io.sparkled.model.entity.Playlist;
import io.sparkled.model.playlist.PlaylistSummary;
import io.sparkled.persistence.playlist.PlaylistPersistenceService;

import javax.inject.Inject;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;

public class PlaylistSearchViewModelConverterImpl extends PlaylistSearchViewModelConverter {

    private final PlaylistPersistenceService playlistPersistenceService;

    @Inject
    public PlaylistSearchViewModelConverterImpl(PlaylistPersistenceService playlistPersistenceService) {
        this.playlistPersistenceService = playlistPersistenceService;
    }

    @Override
    public List<PlaylistSearchViewModel> toViewModels(Collection<Playlist> models) {
        Map<Integer, PlaylistSummary> playlistSummaries = playlistPersistenceService.getPlaylistSummaries();

        return models.stream()
                .map(model -> toViewModel(model, playlistSummaries))
                .collect(toList());
    }

    private PlaylistSearchViewModel toViewModel(Playlist model, Map<Integer, PlaylistSummary> playlistSummaries) {
        PlaylistSummary summary = playlistSummaries.get(model.getId());

        return new PlaylistSearchViewModel()
                .setId(model.getId())
                .setName(model.getName())
                .setSequenceCount(summary.getSequenceCount())
                .setDurationSeconds(summary.getDurationSeconds());
    }
}
