package io.sparkled.model

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.MappedProperty
import io.micronaut.data.annotation.TypeDef
import io.micronaut.data.model.DataType
import io.sparkled.model.embedded.Point2d
import io.sparkled.model.enumeration.StagePropType
import io.sparkled.model.util.IdUtils.uniqueId
import jakarta.inject.Singleton
import jakarta.persistence.AttributeConverter
import jakarta.persistence.Id
import java.time.Instant

@MappedEntity("STAGE_PROP")
data class StagePropModel(
    @Id
    override var id: UniqueId = uniqueId(),
    override var createdAt: Instant = Instant.now(),
    override var updatedAt: Instant = Instant.now(),

    var stageId: String,

    var code: String,
    var name: String,
    var type: StagePropType,
    var groupCode: String? = null,
    var groupDisplayOrder: Int = 0,
    var displayOrder: Int = 0,
    var ledCount: Int = 1,
    @MappedProperty(type = DataType.BOOLEAN)
    var reverse: Boolean = false,
    var positionX: Int = 0,
    var positionY: Int = 0,
    var scaleX: Double = 1.0,
    var scaleY: Double = 1.0,
    var rotation: Double = 0.0,
    var brightness: Int = MAX_BRIGHTNESS,
    var ledPositions: LedPositions = LedPositions.empty,
) : Model {
    companion object {
        const val MAX_BRIGHTNESS = 100
    }
}

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

    override fun convertToDatabaseColumn(attribute: LedPositions?): String {
        return objectMapper.writeValueAsString(attribute ?: emptyList<String>())
    }

    override fun convertToEntityAttribute(dbData: String?): LedPositions? {
        return if (dbData == null) {
            null
        } else {
            val list = objectMapper.readValue<List<Point2d>>(dbData)
            LedPositions.of(list)
        }
    }
}
