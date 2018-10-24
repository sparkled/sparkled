package io.sparkled.model.entity;

public enum SequenceStatus {
    /**
     * Has no animation data whatsoever, cannot be played.
     */
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
