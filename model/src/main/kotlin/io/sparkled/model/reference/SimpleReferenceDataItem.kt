package io.sparkled.model.reference

data class SimpleReferenceDataItem<T : Enum<T>>(
    override val code: T,
    override val name: String
) : ReferenceDataItem<T>
