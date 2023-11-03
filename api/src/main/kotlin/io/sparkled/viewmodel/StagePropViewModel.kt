package io.sparkled.viewmodel

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.sparkled.model.StagePropModel
import io.sparkled.model.UniqueId
import io.sparkled.model.enumeration.StagePropType

data class StagePropViewModel(
    val id: UniqueId,
    val stageId: UniqueId,
    val code: String,
    val name: String,
    val type: StagePropType,
    val ledCount: Int,
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
    val ledPositions: List<Point2dViewModel> = emptyList(),
) : ViewModel {
    fun toModel(objectMapper: ObjectMapper) = StagePropModel(
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
        ledPositionsJson = objectMapper.writeValueAsString(ledPositions),
    )

    companion object {
        fun fromModel(model: StagePropModel, objectMapper: ObjectMapper) = StagePropViewModel(
            id = model.id,
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
            groupCode = model.groupCode,
            groupDisplayOrder = model.groupDisplayOrder,
            ledPositions = objectMapper.readValue(model.ledPositionsJson),
        )
    }
}