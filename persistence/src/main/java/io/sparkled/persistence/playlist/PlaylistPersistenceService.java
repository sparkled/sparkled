package io.sparkled.persistence.playlist;

import io.sparkled.model.entity.Playlist;
import io.sparkled.model.entity.PlaylistSequence;
import io.sparkled.model.entity.PlaylistSummary;
import io.sparkled.model.entity.Sequence;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface PlaylistPersistenceService {

    Playlist createPlaylist(Playlist playlist);

    List<Playlist> getAllPlaylists();

    Map<Integer, PlaylistSummary> getPlaylistSummaries();

    Optional<Playlist> getPlaylistById(int playlistId);

    Optional<Sequence> getSequenceAtPlaylistIndex(int playlistId, int index);

    List<PlaylistSequence> getPlaylistSequencesByPlaylistId(int playlistId);

    Optional<PlaylistSequence> getPlaylistSequenceByUuid(int sequenceId, UUID uuid);

    void savePlaylist(Playlist playlist, List<PlaylistSequence> playlistSequences);

    void deletePlaylist(int playlistId);
}
