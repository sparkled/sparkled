package io.sparkled.persistence.playlist.impl;

import com.google.inject.persist.Transactional;
import io.sparkled.model.entity.Playlist;
import io.sparkled.model.entity.Sequence;
import io.sparkled.persistence.QueryFactory;
import io.sparkled.persistence.playlist.PlaylistPersistenceService;
import io.sparkled.persistence.playlist.impl.query.GetAllPlaylistsQuery;
import io.sparkled.persistence.playlist.impl.query.GetPlaylistByIdQuery;
import io.sparkled.persistence.playlist.impl.query.GetSequenceAtPlaylistIndexQuery;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;

public class PlaylistPersistenceServiceImpl implements PlaylistPersistenceService {

    private QueryFactory queryFactory;

    @Inject
    public PlaylistPersistenceServiceImpl(QueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    @Transactional
    public Optional<Playlist> getPlaylistById(int playlistId) {
        return new GetPlaylistByIdQuery(playlistId).perform(queryFactory);
    }

    @Override
    @Transactional
    public List<Playlist> getAllPlaylists() {
        return new GetAllPlaylistsQuery().perform(queryFactory);
    }

    @Override
    @Transactional
    public Optional<Sequence> getSequenceAtPlaylistIndex(int playlistId, int index) {
        return new GetSequenceAtPlaylistIndexQuery(playlistId, index).perform(queryFactory);
    }
}
