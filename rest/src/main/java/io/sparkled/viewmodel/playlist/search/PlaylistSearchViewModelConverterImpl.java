package io.sparkled.viewmodel.playlist.search;

import io.sparkled.model.entity.Playlist;

public class PlaylistSearchViewModelConverterImpl extends PlaylistSearchViewModelConverter {

    @Override
    public PlaylistSearchViewModel toViewModel(Playlist model) {
        return new PlaylistSearchViewModel()
                .setId(model.getId())
                .setName(model.getName());
    }
}
