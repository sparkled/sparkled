package io.sparkled.persistence.playlist;

import io.sparkled.model.entity.Playlist;
import io.sparkled.model.entity.Sequence;

import java.util.List;
import java.util.Optional;

public interface PlaylistPersistenceService {

    Optional<Playlist> getPlaylistById(int playlistId);

    List<Playlist> getAllPlaylists();

    Optional<Sequence> getSequenceAtPlaylistIndex(int playlistId, int index);
}
