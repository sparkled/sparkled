package io.sparkled.persistence.playlist.impl.query

import io.sparkled.model.entity.QPlaylist.Companion.playlist
import io.sparkled.model.util.TupleUtils
import io.sparkled.persistence.PersistenceQuery
import io.sparkled.persistence.QueryFactory

class GetPlaylistNamesQuery : PersistenceQuery<Map<Int, String>> {

    override fun perform(queryFactory: QueryFactory): Map<Int, String> {
        return queryFactory
            .select(playlist.id, playlist.name)
            .from(playlist)
            .fetch()
            .associateBy(
                { TupleUtils.getInt(it, 0) },
                { TupleUtils.getString(it, 1) }
            )
    }
}
