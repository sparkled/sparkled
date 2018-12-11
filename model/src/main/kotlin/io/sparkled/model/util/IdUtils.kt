package io.sparkled.model.util

import java.util.UUID

object IdUtils {

    /**
     * An ID value that will never exist in the database.
     */
    const val NO_ID: Int = -1

    /**
     * Useful for IN queries where no IDs will match.
     */
    val NO_IDS: List<Int> = listOf(NO_ID)

    /**
     * A UUID value that will never exist in the database.
     */
    val NO_UUID = UUID(0, 0)

    /**
     * Useful for IN queries where no UUIDs will match.
     */
    val NO_UUIDS: List<UUID> = listOf(NO_UUID)
}
