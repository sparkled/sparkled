package io.sparkled.model

import io.micronaut.data.annotation.MappedEntity
import io.sparkled.model.util.IdUtils.uniqueId
import jakarta.persistence.Id
import java.time.Instant

@MappedEntity("SETTING")
data class SettingModel(
    @Id
    override var id: UniqueId = uniqueId(),
    override var createdAt: Instant = Instant.now(),
    override var updatedAt: Instant = Instant.now(),

    var code: String,
    var value: String,
) : Model
