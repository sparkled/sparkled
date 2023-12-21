package io.sparkled.model.embedded

import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
data class Rectangle(
    val x1: Double,
    val y1: Double,
    val x2: Double,
    val y2: Double,
)