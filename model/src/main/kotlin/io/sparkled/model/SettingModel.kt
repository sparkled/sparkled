package io.sparkled.model

import io.micronaut.data.annotation.MappedEntity
import jakarta.persistence.Id
import java.time.Instant

@MappedEntity("SEQUENCE")
data class SettingModel(
    @Id
    override var id: String,
    override var createdAt: Instant = Instant.now(),
    override var updatedAt: Instant = Instant.now(),

    var code: String,
    var value: String,
) : Model
