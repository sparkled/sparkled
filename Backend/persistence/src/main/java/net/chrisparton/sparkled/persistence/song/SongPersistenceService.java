package net.chrisparton.sparkled.persistence.song;

import net.chrisparton.sparkled.entity.RenderedSong;
import net.chrisparton.sparkled.entity.Song;
import net.chrisparton.sparkled.entity.SongData;

import java.util.List;
import java.util.Optional;

public interface SongPersistenceService {

    List<Song> getAllSongs();

    boolean removeSongAndData(int songId);

    Optional<Song> getSongById(int songId);

    Optional<SongData> getSongDataById(int songId);

    Optional<RenderedSong> getRenderedSongById(int songId);

    int saveSong(Song song, RenderedSong renderedSong);

    int saveSongData(SongData songData);
}
