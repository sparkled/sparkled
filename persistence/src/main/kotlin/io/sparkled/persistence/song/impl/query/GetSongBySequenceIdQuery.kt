package io.sparkled.persistence.song.impl.query

import io.sparkled.model.entity.QSequence.sequence
import io.sparkled.model.entity.QSong.song
import io.sparkled.model.entity.Song
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import java.util.Optional

class GetSongBySequenceIdQuery(private val sequenceId: Int) : PersistenceQuery<Optional<Song>> {

    override fun perform(queryFactory: QueryFactory): Optional<Song> {
        val song = queryFactory
            .select(song)
            .from(sequence)
            .innerJoin(song).on(sequence.songId.eq(song.id))
            .where(sequence.id.eq(sequenceId))
            .fetchFirst()

        return Optional.ofNullable(song)
    }
}
