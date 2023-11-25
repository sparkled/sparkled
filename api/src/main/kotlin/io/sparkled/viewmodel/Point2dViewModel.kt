package io.sparkled.viewmodel

import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
data class Point2dViewModel(
    val x: Double,
    val y: Double,
) : ViewModel
