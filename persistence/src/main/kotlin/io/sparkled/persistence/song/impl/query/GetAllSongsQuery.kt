package io.sparkled.persistence.song.impl.query

import io.sparkled.model.entity.Song
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.PersistenceQuery.Companion.qSong
import io.sparkled.persistence.QueryFactory

class GetAllSongsQuery : PersistenceQuery<List<Song>> {

    override fun perform(queryFactory: QueryFactory): List<Song> {
        return queryFactory
                .selectFrom(qSong)
                .orderBy(
                        qSong.name.asc(),
                        qSong.album.asc(),
                        qSong.artist.asc()
                )
                .fetch()
    }
}
