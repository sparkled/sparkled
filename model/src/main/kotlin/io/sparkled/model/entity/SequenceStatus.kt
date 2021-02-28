package io.sparkled.model.entity

import com.fasterxml.jackson.annotation.JsonEnumDefaultValue

enum class SequenceStatus {
    /**
     * Has no animation data, cannot be played.
     */
    @JsonEnumDefaultValue
    NEW,

    /**
     * Has unpublished animation data, can be played with the previously published data.
     */
    DRAFT,

    /**
     * Has up-to-date animation data, can be played.
     */
    PUBLISHED
}
