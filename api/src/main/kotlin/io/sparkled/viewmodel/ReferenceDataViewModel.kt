package io.sparkled.viewmodel

import io.sparkled.model.animation.param.Param
import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
data class ReferenceDataViewModel(
    val blendModes: List<EditorItemViewModel>,
    val easings: List<EditorItemViewModel>,
    val effects: List<EditorItemViewModel>,
    val fills: List<EditorItemViewModel>
) : ViewModel

@GenerateClientType
data class EditorItemViewModel(
    val code: String = "NONE",
    val name: String = "None",
    val params: List<Param> = emptyList()
) : ViewModel
