package io.sparkled.model.reference

interface ReferenceDataItem<T : Enum<T>> {
    val code: T
    val name: String
}
