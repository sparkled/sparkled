package io.sparkled.persistence.playlist;

import io.sparkled.model.entity.Playlist;
import io.sparkled.model.entity.Sequence;

import java.util.Optional;

public interface PlaylistPersistenceService {

    // TODO: Remove this method once scheduling & playlist UI are implemented.
    Optional<Playlist> getFirstPlaylist();

    Optional<Sequence> getSequenceAtPlaylistIndex(int playlistId, int index);
}
