package io.sparkled.model.playlist

class PlaylistSummary {

    private var sequenceCount: Int? = null
    private var durationSeconds: Int? = null

    fun getSequenceCount(): Int? {
        return sequenceCount
    }

    fun setSequenceCount(sequenceCount: Int): PlaylistSummary {
        this.sequenceCount = sequenceCount
        return this
    }

    fun getDurationSeconds(): Int? {
        return durationSeconds
    }

    fun setDurationSeconds(durationSeconds: Int): PlaylistSummary {
        this.durationSeconds = durationSeconds
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlaylistSummary

        if (sequenceCount != other.sequenceCount) return false
        if (durationSeconds != other.durationSeconds) return false

        return true
    }

    override fun hashCode(): Int {
        var result = sequenceCount?.hashCode() ?: 0
        result = 31 * result + (durationSeconds?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "PlaylistSummary(sequenceCount=$sequenceCount, durationSeconds=$durationSeconds)"
    }
}
