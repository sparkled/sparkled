package io.sparkled.viewmodel.error

import io.sparkled.model.UniqueId
import io.sparkled.model.annotation.GenerateClientType
import io.sparkled.model.util.IdUtils.uniqueId
import io.sparkled.viewmodel.ViewModel

@GenerateClientType
data class ErrorViewModel(
    val id: UniqueId = uniqueId(),
    val code: ApiErrorCode,
    val userMessage: String = code.userMessage,
    val devMessage: String? = null,
) : ViewModel
