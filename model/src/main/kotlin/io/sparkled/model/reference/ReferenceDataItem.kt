package io.sparkled.model.reference

data class ReferenceDataItem<T : Enum<T>>(
    val code: T,
    val name: String
)
