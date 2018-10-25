package io.sparkled.viewmodel.playlist.search

import io.sparkled.viewmodel.ViewModel

import java.util.Objects

class PlaylistSearchViewModel : ViewModel {

    private var id: Integer? = null
    private var name: String? = null
    private var sequenceCount: Integer? = null
    private var durationSeconds: Integer? = null

    fun getId(): Integer {
        return id
    }

    fun setId(id: Integer): PlaylistSearchViewModel {
        this.id = id
        return this
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String): PlaylistSearchViewModel {
        this.name = name
        return this
    }

    fun getSequenceCount(): Integer {
        return sequenceCount
    }

    fun setSequenceCount(sequenceCount: Integer): PlaylistSearchViewModel {
        this.sequenceCount = sequenceCount
        return this
    }

    fun getDurationSeconds(): Integer {
        return durationSeconds
    }

    fun setDurationSeconds(durationSeconds: Integer): PlaylistSearchViewModel {
        this.durationSeconds = durationSeconds
        return this
    }

    @Override
    fun equals(o: Object?): Boolean {
        if (this === o) return true
        if (o == null || getClass() !== o!!.getClass()) return false
        val that = o
        return Objects.equals(id, that!!.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(sequenceCount, that.sequenceCount) &&
                Objects.equals(durationSeconds, that.durationSeconds)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(id, name, sequenceCount, durationSeconds)
    }

    @Override
    fun toString(): String {
        return "PlaylistSearchViewModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sequenceCount=" + sequenceCount +
                ", durationSeconds=" + durationSeconds +
                '}'
    }
}
