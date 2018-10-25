package io.sparkled.persistence.song.impl.query

import io.sparkled.model.entity.Song
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

import java.util.Optional

class GetSongBySequenceIdQuery(private val sequenceId: Int) : PersistenceQuery<Optional<Song>> {

    @Override
    fun perform(queryFactory: QueryFactory): Optional<Song> {
        val song = queryFactory
                .select(qSong)
                .from(qSequence)
                .innerJoin(qSong).on(qSequence.songId.eq(qSong.id))
                .where(qSequence.id.eq(sequenceId))
                .fetchFirst()

        return Optional.ofNullable(song)
    }
}
