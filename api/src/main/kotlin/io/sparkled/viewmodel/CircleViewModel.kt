package io.sparkled.viewmodel

import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
data class CircleViewModel(
    val x: Double,
    val y: Double,
    val r: Double,
) : ViewModel
