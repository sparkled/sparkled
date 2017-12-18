package io.sparkled.persistence.song;

import io.sparkled.model.entity.RenderedSong;
import io.sparkled.model.entity.Song;
import io.sparkled.model.entity.SongAnimation;
import io.sparkled.model.entity.SongAudio;

import java.util.List;
import java.util.Optional;

public interface SongPersistenceService {

    List<Song> getAllSongs();

    Integer deleteSong(int songId);

    Optional<Song> getSongById(int songId);

    Optional<SongAudio> getSongDataById(int songId);

    Optional<SongAnimation> getSongAnimationById(int songId);

    Optional<RenderedSong> getRenderedSongById(int songId);

    Integer saveSong(Song song);

    Integer saveSongAudio(SongAudio songAudio);

    Integer saveSongAnimation(SongAnimation songAnimation);

    Integer saveRenderedSong(RenderedSong renderedSong);
}
