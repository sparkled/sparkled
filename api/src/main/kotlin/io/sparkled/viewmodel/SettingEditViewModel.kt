package io.sparkled.viewmodel

import io.sparkled.model.annotation.GenerateClientType

@GenerateClientType
data class SettingEditViewModel(
    val value: String,
) : ViewModel
