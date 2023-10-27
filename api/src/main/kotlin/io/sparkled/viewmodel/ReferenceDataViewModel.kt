package io.sparkled.viewmodel

import io.sparkled.model.animation.param.Param

data class ReferenceDataViewModel(
    val blendModes: List<EditorItemViewModel>,
    val easings: List<EditorItemViewModel>,
    val effects: List<EditorItemViewModel>,
    val fills: List<EditorItemViewModel>
) : ViewModel

data class EditorItemViewModel(
    val code: String = "NONE",
    val name: String = "None",
    val params: List<Param> = emptyList()
) : ViewModel
