package io.sparkled.model

import java.time.Instant

/** Sparkled uses string-based IDs. This type alias is used to differentiate IDs from other string fields. */
typealias UniqueId = String

sealed interface Model {
    val id: UniqueId
    val createdAt: Instant
    val updatedAt: Instant
}
