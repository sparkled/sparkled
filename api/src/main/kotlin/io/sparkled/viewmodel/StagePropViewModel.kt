package io.sparkled.viewmodel

import io.sparkled.model.StagePropModel
import io.sparkled.model.UniqueId
import io.sparkled.model.annotation.GenerateClientType
import io.sparkled.model.embedded.PixelPositions
import io.sparkled.model.enumeration.StagePropType

@GenerateClientType
data class StagePropViewModel(
    val id: UniqueId,
    val stageId: UniqueId,
    val code: String,
    val name: String,
    val type: StagePropType,
    val ledCount: Int,

    /** Computed field, will be zero unless this prop is a non-initial item in a group. */
    val ledOffset: Int = 0,

    val reverse: Boolean,
    val positionX: Int,
    val positionY: Int,
    val scaleX: Double,
    val scaleY: Double,
    val rotation: Double,
    val brightness: Int,
    val displayOrder: Int,
    val groupCode: String? = null,
    val groupDisplayOrder: Int? = null,
    val ledPositions: PixelPositions = PixelPositions.empty,
) : ViewModel {
    fun toModel() = StagePropModel(
        id = id,
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
        displayOrder = displayOrder,
        groupCode = groupCode,
        groupDisplayOrder = groupDisplayOrder ?: 0,
        ledPositions = ledPositions,
    )

    companion object {
        fun fromModel(model: StagePropModel, stageProps: Collection<StagePropModel>): StagePropViewModel {
            val ledOffset = if (model.groupCode == null) 0 else {
                stageProps
                    .filter { it.groupCode == model.groupCode && it.groupDisplayOrder < model.groupDisplayOrder }
                    .sumOf { it.ledCount }
            }

            return StagePropViewModel(
                id = model.id,
                stageId = model.stageId,
                code = model.code,
                name = model.name,
                type = model.type,
                ledCount = model.ledCount,
                ledOffset = ledOffset,
                reverse = model.reverse,
                positionX = model.positionX,
                positionY = model.positionY,
                scaleX = model.scaleX,
                scaleY = model.scaleY,
                rotation = model.rotation,
                brightness = model.brightness,
                displayOrder = model.displayOrder,
                groupCode = model.groupCode,
                groupDisplayOrder = model.groupDisplayOrder,
                ledPositions = model.ledPositions,
            )
        }
    }
}
