package io.sparkled.model.embedded

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.micronaut.core.convert.ConversionContext
import io.micronaut.data.annotation.TypeDef
import io.micronaut.data.model.DataType
import io.micronaut.data.model.runtime.convert.AttributeConverter
import jakarta.inject.Singleton

@TypeDef(type = DataType.STRING, converter = LedPositionConverter::class)
class LedPositions(initialCapacity: Int) : ArrayList<Point2d>(initialCapacity) {
    companion object {
        fun of(vararg ledPositions: Point2d) = LedPositions(ledPositions.size).apply {
            addAll(ledPositions)
        }

        fun of(ledPositions: Collection<Point2d>) = LedPositions(ledPositions.size).apply {
            addAll(ledPositions)
        }

        val empty = LedPositions(0)
    }
}

@Singleton
class LedPositionConverter(
    private val objectMapper: ObjectMapper,
) : AttributeConverter<LedPositions, String> {

    override fun convertToPersistedValue(entityValue: LedPositions?, context: ConversionContext?) =
        entityValue?.let(objectMapper::writeValueAsString)

    override fun convertToEntityValue(persistedValue: String?, context: ConversionContext?) =
        persistedValue?.let { LedPositions.of(objectMapper.readValue<List<Point2d>>(persistedValue)) }
}
