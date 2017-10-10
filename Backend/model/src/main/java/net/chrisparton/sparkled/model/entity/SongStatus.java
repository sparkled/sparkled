package net.chrisparton.sparkled.model.entity;

public enum SongStatus {
    /**
     * Has no animation data whatsoever, cannot be scheduled for playback.
     */
    NEW,

    /**
     * Has unpublished animation data, can be scheduled for playback with the previously published data.
     */
    DRAFT,

    /**
     * Has up-to-date animation data, can be scheduled for playback.
     */
    PUBLISHED
}
