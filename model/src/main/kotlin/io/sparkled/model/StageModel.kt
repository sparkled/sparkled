package io.sparkled.model

import io.micronaut.core.convert.ConversionContext
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.MappedProperty
import io.micronaut.data.model.runtime.convert.AttributeConverter
import io.sparkled.model.converter.InstantConverter
import io.sparkled.model.util.IdUtils.uniqueId
import jakarta.inject.Singleton
import jakarta.persistence.Id
import java.time.Instant

@MappedEntity("STAGE")
data class StageModel(
    @Id
    override var id: UniqueId = uniqueId(),

    @MappedProperty(converter = InstantConverter::class)
    override var createdAt: Instant = Instant.now(),

    @MappedProperty(converter = InstantConverter::class)
    override var updatedAt: Instant = Instant.now(),

    var name: String,
    var width: Int,
    var height: Int,
) : Model