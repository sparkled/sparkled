package io.sparkled.persistence.song;

import io.sparkled.model.entity.Song;
import io.sparkled.model.entity.SongAnimation;
import io.sparkled.model.entity.SongAudio;
import io.sparkled.model.render.RenderedChannelMap;

import java.util.List;
import java.util.Optional;

public interface SongPersistenceService {

    List<Song> getAllSongs();

    Integer deleteSong(int songId);

    Optional<Song> getSongById(int songId);

    Optional<SongAudio> getSongDataById(int songId);

    Optional<SongAnimation> getSongAnimationById(int songId);

    Integer saveSong(Song song);

    Integer saveSongAudio(SongAudio songAudio);

    Integer saveSongAnimation(SongAnimation songAnimation);

    RenderedChannelMap getRenderedChannels(Song song);

    void saveRenderedChannels(Song song, RenderedChannelMap renderedChannelMap);

    void deleteRenderedChannels(int songId);
}
