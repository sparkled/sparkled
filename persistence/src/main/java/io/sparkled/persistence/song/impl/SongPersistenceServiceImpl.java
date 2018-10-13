package io.sparkled.persistence.song.impl;

import io.sparkled.model.entity.Song;
import io.sparkled.model.entity.SongAudio;
import io.sparkled.persistence.QueryFactory;
import io.sparkled.persistence.sequence.impl.query.GetSongAudioBySequenceIdQuery;
import io.sparkled.persistence.song.SongPersistenceService;
import io.sparkled.persistence.song.impl.query.*;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class SongPersistenceServiceImpl implements SongPersistenceService {

    private QueryFactory queryFactory;

    @Inject
    public SongPersistenceServiceImpl(QueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Song createSong(Song song, byte[] audioData) {
        Song savedSong = new SaveSongQuery(song).perform(queryFactory);
        new SaveSongAudioQuery(savedSong, audioData).perform(queryFactory);
        return savedSong;
    }

    @Override
    public List<Song> getAllSongs() {
        return new GetAllSongsQuery().perform(queryFactory);
    }

    @Override
    public Optional<Song> getSongById(int songId) {
        return new GetSongByIdQuery(songId).perform(queryFactory);
    }

    @Override
    public Optional<Song> getSongBySequenceId(int sequenceId) {
        return new GetSongBySequenceIdQuery(sequenceId).perform(queryFactory);
    }

    @Override
    public void deleteSong(int songId) {
        new DeleteSongsQuery(Collections.singletonList(songId)).perform(queryFactory);
    }
}
