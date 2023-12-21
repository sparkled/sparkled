package io.sparkled.model.embedded

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.micronaut.core.convert.ConversionContext
import io.micronaut.data.annotation.TypeDef
import io.micronaut.data.model.DataType
import io.micronaut.data.model.runtime.convert.AttributeConverter
import io.sparkled.model.annotation.GenerateClientType
import jakarta.inject.Singleton

@GenerateClientType
@TypeDef(type = DataType.STRING, converter = LedPositionConverter::class)
data class PixelPositions(
    val points: List<Point2d>,
    val bounds: Rectangle,
) {
    companion object {
        val empty = PixelPositions(
            points = emptyList(),
            bounds = Rectangle(0.0, 0.0, 0.0, 0.0)
        )
    }
}

@Singleton
class LedPositionConverter(
    private val objectMapper: ObjectMapper,
) : AttributeConverter<PixelPositions, String> {

    override fun convertToPersistedValue(entityValue: PixelPositions?, context: ConversionContext?) =
        entityValue?.let(objectMapper::writeValueAsString)

    override fun convertToEntityValue(persistedValue: String?, context: ConversionContext?) =
        persistedValue?.let { objectMapper.readValue<PixelPositions>(persistedValue) }
}
