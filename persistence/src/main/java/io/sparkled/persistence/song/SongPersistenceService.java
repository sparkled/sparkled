package io.sparkled.persistence.song;

import io.sparkled.model.entity.Song;
import io.sparkled.model.entity.SongAnimation;
import io.sparkled.model.entity.SongAudio;
import io.sparkled.model.render.RenderedStagePropDataMap;

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

    RenderedStagePropDataMap getRenderedStageProps(Song song);

    void saveRenderedChannels(Song song, RenderedStagePropDataMap renderedStagePropDataMap);

    void deleteRenderedStageProps(int songId);
}
