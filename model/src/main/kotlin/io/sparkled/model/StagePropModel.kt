package io.sparkled.model

import io.micronaut.data.annotation.MappedEntity
import io.micronaut.data.annotation.MappedProperty
import io.micronaut.data.model.DataType
import io.sparkled.model.converter.InstantConverter
import io.sparkled.model.embedded.PixelPositions
import io.sparkled.model.enumeration.StagePropType
import io.sparkled.model.util.IdUtils.uniqueId
import jakarta.persistence.Id
import java.time.Instant

@MappedEntity("STAGE_PROP")
data class StagePropModel(
    @Id
    override var id: UniqueId = uniqueId(),

    @MappedProperty(converter = InstantConverter::class)
    override var createdAt: Instant = Instant.now(),

    @MappedProperty(converter = InstantConverter::class)
    override var updatedAt: Instant = Instant.now(),

    var stageId: UniqueId,

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
    var ledPositions: PixelPositions = PixelPositions.empty,
) : Model {
    companion object {
        const val MAX_BRIGHTNESS = 100
    }
}
