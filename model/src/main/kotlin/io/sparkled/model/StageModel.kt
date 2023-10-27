package io.sparkled.model

import io.micronaut.data.annotation.MappedEntity
import jakarta.persistence.Id
import java.time.Instant

@MappedEntity("STAGE")
data class StageModel(
    @Id
    override var id: UniqueId = uniqueId(),
    override var createdAt: Instant = Instant.now(),
    override var updatedAt: Instant = Instant.now(),

    var name: String,
    var width: Int,
    var height: Int,
) : Model
