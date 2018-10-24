package io.sparkled.persistence.song;

import io.sparkled.model.entity.Song;
import io.sparkled.model.entity.SongAudio;

import java.util.List;
import java.util.Optional;

public interface SongPersistenceService {

    Song createSong(Song song, byte[] audioData);

    List<Song> getAllSongs();

    Optional<Song> getSongById(int songId);

    Optional<Song> getSongBySequenceId(int sequenceId);

    void deleteSong(int songId);
}
