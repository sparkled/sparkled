package io.sparkled.model.embedded

import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
data class Point2d(
    val x: Double,
    val y: Double,
)