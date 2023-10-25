package io.sparkled.model

import java.time.Instant

sealed interface Model {
    val id: String
    val createdAt: Instant
    val updatedAt: Instant
}
