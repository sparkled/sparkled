package io.sparkled.model

import io.micronaut.data.annotation.MappedEntity
import io.sparkled.model.enumeration.StagePropType
import io.sparkled.model.util.IdUtils.uniqueId
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
    var reverse: Boolean = false,
    var positionX: Int = 0,
    var positionY: Int = 0,
    var scaleX: Double = 1.0,
    var scaleY: Double = 1.0,
    var rotation: Double = 0.0,
    var brightness: Int = MAX_BRIGHTNESS,
    var ledPositionsJson: String = "[]", // TODO custom converter
) : Model {
    companion object {
        const val MAX_BRIGHTNESS = 100
    }
}
