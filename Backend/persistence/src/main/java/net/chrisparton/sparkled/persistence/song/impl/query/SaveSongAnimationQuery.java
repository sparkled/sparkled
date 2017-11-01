package net.chrisparton.sparkled.persistence.song.impl.query;

import net.chrisparton.sparkled.model.entity.SongAnimation;
import net.chrisparton.sparkled.model.validator.SongAnimationValidator;
import net.chrisparton.sparkled.persistence.PersistenceQuery;

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
