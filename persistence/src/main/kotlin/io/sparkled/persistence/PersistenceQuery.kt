package io.sparkled.persistence

import java.util.Collections
import java.util.UUID

interface PersistenceQuery<out T> {

    fun perform(queryFactory: QueryFactory): T

    companion object {

        /**
         * Useful for IN queries where no UUIDs will match.
         */
        val noUuids: List<UUID> = Collections.singletonList(UUID(0, 0))

        /**
         * Useful for IN queries where no IDs will match.
         */
        val noIds: List<Int> = Collections.singletonList(-1)
    }
}
