package io.sparkled.persistence.song.impl.query

import io.sparkled.model.entity.QSong.song
import io.sparkled.model.entity.Song
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory
import java.util.Optional

class GetSongByIdQuery(private val songId: Int) : PersistenceQuery<Optional<Song>> {

    override fun perform(queryFactory: QueryFactory): Optional<Song> {
        val song = queryFactory
            .selectFrom(song)
            .where(song.id.eq(songId))
            .fetchFirst()

        return Optional.ofNullable(song)
    }
}
