package io.sparkled.persistence.playlist.impl;

import com.google.inject.persist.Transactional;
import io.sparkled.model.entity.Playlist;
import io.sparkled.model.entity.PlaylistSequence;
import io.sparkled.model.entity.Sequence;
import io.sparkled.persistence.QueryFactory;
import io.sparkled.persistence.playlist.PlaylistPersistenceService;
import io.sparkled.persistence.playlist.impl.query.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class PlaylistPersistenceServiceImpl implements PlaylistPersistenceService {

    private QueryFactory queryFactory;

    @Inject
    public PlaylistPersistenceServiceImpl(QueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    @Transactional
    public Integer createPlaylist(Playlist playlist) {
        return new SavePlaylistQuery(playlist).perform(queryFactory);
    }

    @Override
    @Transactional
    public List<Playlist> getAllPlaylists() {
        return new GetAllPlaylistsQuery().perform(queryFactory);
    }

    @Override
    @Transactional
    public Optional<Playlist> getPlaylistById(int playlistId) {
        return new GetPlaylistByIdQuery(playlistId).perform(queryFactory);
    }

    @Override
    @Transactional
    public Optional<Sequence> getSequenceAtPlaylistIndex(int playlistId, int index) {
        return new GetSequenceAtPlaylistIndexQuery(playlistId, index).perform(queryFactory);
    }

    @Override
    @Transactional
    public List<PlaylistSequence> getPlaylistSequencesByPlaylistId(int playlistId) {
        return new GetPlaylistSequencesByPlaylistIdQuery(playlistId).perform(queryFactory);
    }

    @Override
    @Transactional
    public Optional<PlaylistSequence> getPlaylistSequenceByUuid(int sequenceId, UUID uuid) {
        return new GetPlaylistSequenceByUuidQuery(sequenceId, uuid).perform(queryFactory);
    }

    @Override
    @Transactional
    public Integer savePlaylist(Playlist playlist, List<PlaylistSequence> playlistSequences) {
        new SavePlaylistSequencesQuery(playlistSequences).perform(queryFactory);
        return new SavePlaylistQuery(playlist).perform(queryFactory);
    }

    @Override
    @Transactional
    public void deletePlaylist(int playlistId) {
        new DeletePlaylistByIdQuery(playlistId).perform(queryFactory);
    }
}
