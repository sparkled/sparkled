package io.sparkled.model.util

import java.util.UUID

object IdUtils {

    fun newUuid(): UUID = UUID.randomUUID()

    /**
     * An ID value that will never exist in the database.
     */
    const val NO_ID: Int = -1
}
