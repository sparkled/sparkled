package io.sparkled.persistence.song.impl.query;

import io.sparkled.model.entity.SongAnimation;
import io.sparkled.model.validator.SongAnimationValidator;
import io.sparkled.persistence.PersistenceQuery;

import javax.persistence.EntityManager;

public class SaveSongAnimationQuery implements PersistenceQuery<Integer> {

    private final SongAnimation songAnimation;

    public SaveSongAnimationQuery(SongAnimation songAnimation) {
        this.songAnimation = songAnimation;
    }

    @Override
    public Integer perform(EntityManager entityManager) {
        SongAnimationValidator songAnimationValidator = new SongAnimationValidator();
        songAnimationValidator.validate(songAnimation);

        SongAnimation savedSongAnimation = entityManager.merge(songAnimation);
        return savedSongAnimation.getSongId();
    }
}
