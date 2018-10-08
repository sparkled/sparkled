package io.sparkled.persistence.playlist;

import io.sparkled.model.entity.Playlist;
import io.sparkled.model.entity.PlaylistSequence;
import io.sparkled.model.entity.Sequence;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PlaylistPersistenceService {

    Integer createPlaylist(Playlist playlist);

    List<Playlist> getAllPlaylists();

    Optional<Playlist> getPlaylistById(int playlistId);

    Optional<Sequence> getSequenceAtPlaylistIndex(int playlistId, int index);

    List<PlaylistSequence> getPlaylistSequencesByPlaylistId(int playlistId);

    Optional<PlaylistSequence> getPlaylistSequenceByUuid(int sequenceId, UUID uuid);

    Integer savePlaylist(Playlist playlist, List<PlaylistSequence> playlistSequences);

    void deletePlaylist(int playlistId);
}
