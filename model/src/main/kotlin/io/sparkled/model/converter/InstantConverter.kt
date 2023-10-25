package io.sparkled.model.converter

import io.micronaut.core.convert.ConversionContext
import io.micronaut.data.model.runtime.convert.AttributeConverter
import jakarta.inject.Singleton
import java.time.Instant

@Singleton
class InstantConverter : AttributeConverter<Instant?, String?> {
    override fun convertToPersistedValue(entityValue: Instant?, context: ConversionContext?) = entityValue?.toString()

    override fun convertToEntityValue(persistedValue: String?, context: ConversionContext?) =
        persistedValue?.let(Instant::parse)
}
