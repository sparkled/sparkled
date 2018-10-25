package io.sparkled.model.playlist

import java.util.Objects

class PlaylistSummary {

    private var sequenceCount: Integer? = null
    private var durationSeconds: Integer? = null

    fun getSequenceCount(): Integer {
        return sequenceCount
    }

    fun setSequenceCount(sequenceCount: Integer): PlaylistSummary {
        this.sequenceCount = sequenceCount
        return this
    }

    fun getDurationSeconds(): Integer {
        return durationSeconds
    }

    fun setDurationSeconds(durationSeconds: Integer): PlaylistSummary {
        this.durationSeconds = durationSeconds
        return this
    }

    @Override
    fun equals(o: Object?): Boolean {
        if (this === o) return true
        if (o == null || getClass() !== o!!.getClass()) return false
        val that = o
        return Objects.equals(sequenceCount, that!!.sequenceCount) && Objects.equals(durationSeconds, that.durationSeconds)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(sequenceCount, durationSeconds)
    }

    @Override
    fun toString(): String {
        return "PlaylistSummary{" +
                "sequenceCount=" + sequenceCount +
                ", durationSeconds=" + durationSeconds +
                '}'
    }
}
