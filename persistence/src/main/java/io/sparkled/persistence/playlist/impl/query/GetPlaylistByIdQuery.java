package io.sparkled.persistence.playlist.impl.query;

import io.sparkled.model.entity.Playlist;
import io.sparkled.persistence.PersistenceQuery;
import io.sparkled.persistence.QueryFactory;

import java.util.Optional;

public class GetPlaylistByIdQuery implements PersistenceQuery<Optional<Playlist>> {

    private final int playlistId;

    public GetPlaylistByIdQuery(int playlistId) {
        this.playlistId = playlistId;
    }

    @Override
    public Optional<Playlist> perform(QueryFactory queryFactory) {
        final Playlist playlist = queryFactory
                .selectFrom(qPlaylist)
                .where(qPlaylist.id.eq(playlistId))
                .fetchFirst();

        return Optional.ofNullable(playlist);
    }
}
