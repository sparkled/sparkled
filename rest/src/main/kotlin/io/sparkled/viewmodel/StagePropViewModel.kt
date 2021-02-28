package io.sparkled.viewmodel

import io.sparkled.model.entity.v2.StagePropEntity
import io.sparkled.model.util.IdUtils
import java.util.*

data class StagePropViewModel(
    val uuid: UUID = IdUtils.newUuid(),
    val stageId: Int,
    val code: String,
    val name: String,
    val type: String,
    val ledCount: Int,
    val reverse: Boolean,
    val positionX: Int,
    val positionY: Int,
    val scaleX: Float,
    val scaleY: Float,
    val rotation: Int,
    val brightness: Int,
    val displayOrder: Int
) {
    fun toModel() = StagePropEntity(
        uuid = uuid,
        stageId = stageId,
        code = code,
        name = name,
        type = type,
        ledCount = ledCount,
        reverse = reverse,
        positionX = positionX,
        positionY = positionY,
        scaleX = scaleX,
        scaleY = scaleY,
        rotation = rotation,
        brightness = brightness,
        displayOrder = displayOrder
    )

    companion object {
        fun fromModel(model: StagePropEntity) = StagePropViewModel(
            uuid = model.uuid,
            stageId = model.stageId,
            code = model.code,
            name = model.name,
            type = model.type,
            ledCount = model.ledCount,
            reverse = model.reverse,
            positionX = model.positionX,
            positionY = model.positionY,
            scaleX = model.scaleX,
            scaleY = model.scaleY,
            rotation = model.rotation,
            brightness = model.brightness,
            displayOrder = model.displayOrder,
        )
    }
}
