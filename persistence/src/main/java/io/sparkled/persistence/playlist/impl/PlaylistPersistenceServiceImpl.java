package io.sparkled.persistence.playlist.impl;

import io.sparkled.model.entity.Playlist;
import io.sparkled.model.entity.PlaylistSequence;
import io.sparkled.model.entity.PlaylistSummary;
import io.sparkled.model.entity.Sequence;
import io.sparkled.persistence.QueryFactory;
import io.sparkled.persistence.playlist.PlaylistPersistenceService;
import io.sparkled.persistence.playlist.impl.query.*;

import javax.inject.Inject;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class PlaylistPersistenceServiceImpl implements PlaylistPersistenceService {

    private QueryFactory queryFactory;

    @Inject
    public PlaylistPersistenceServiceImpl(QueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Playlist createPlaylist(Playlist playlist) {
        return new SavePlaylistQuery(playlist).perform(queryFactory);
    }

    @Override
    public List<Playlist> getAllPlaylists() {
        return new GetAllPlaylistsQuery().perform(queryFactory);
    }

    @Override
    public Map<Integer, PlaylistSummary> getPlaylistSummaries() {
        return new GetPlaylistSummariesQuery().perform(queryFactory);
    }

    @Override
    public Optional<Playlist> getPlaylistById(int playlistId) {
        return new GetPlaylistByIdQuery(playlistId).perform(queryFactory);
    }

    @Override
    public Optional<Sequence> getSequenceAtPlaylistIndex(int playlistId, int index) {
        return new GetSequenceAtPlaylistIndexQuery(playlistId, index).perform(queryFactory);
    }

    @Override
    public List<PlaylistSequence> getPlaylistSequencesByPlaylistId(int playlistId) {
        return new GetPlaylistSequencesByPlaylistIdQuery(playlistId).perform(queryFactory);
    }

    @Override
    public Optional<PlaylistSequence> getPlaylistSequenceByUuid(int sequenceId, UUID uuid) {
        return new GetPlaylistSequenceByUuidQuery(sequenceId, uuid).perform(queryFactory);
    }

    @Override
    public void savePlaylist(Playlist playlist, List<PlaylistSequence> playlistSequences) {
        playlist = new SavePlaylistQuery(playlist).perform(queryFactory);
        new SavePlaylistSequencesQuery(playlist, playlistSequences).perform(queryFactory);
    }

    @Override
    public void deletePlaylist(int playlistId) {
        new DeletePlaylistQuery(playlistId).perform(queryFactory);
    }
}
