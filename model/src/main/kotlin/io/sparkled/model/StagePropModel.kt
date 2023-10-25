package io.sparkled.model

import io.micronaut.data.annotation.MappedEntity
import io.sparkled.model.enumeration.StagePropType
import jakarta.persistence.Id
import java.time.Instant

@MappedEntity("STAGE_PROP")
data class StagePropModel(
    @Id
    override var id: String,
    override var createdAt: Instant = Instant.now(),
    override var updatedAt: Instant = Instant.now(),

    var stageId: String,

    var code: String,
    var name: String,
    var groupCode: String? = null,
    var groupDisplayOrder: Int = 0,
    var type: StagePropType,
    var ledCount: Int,
    var reverse: Boolean = false,
    var positionX: Int,
    var positionY: Int,
    var scaleX: Double = 1.0,
    var scaleY: Double = 1.0,
    var rotation: Int = 0,
    var brightness: Int = 100, // TODO move to constant
    var displayOrder: Int = 0,
    var ledPositionsJson: String = "[]",
) : Model
