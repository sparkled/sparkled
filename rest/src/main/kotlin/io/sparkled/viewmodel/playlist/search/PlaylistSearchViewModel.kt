package io.sparkled.viewmodel.playlist.search

import io.sparkled.viewmodel.ViewModel

class PlaylistSearchViewModel : ViewModel {

    private var id: Int? = null
    private var name: String? = null
    private var sequenceCount: Int? = null
    private var durationSeconds: Int? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?): PlaylistSearchViewModel {
        this.id = id
        return this
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?): PlaylistSearchViewModel {
        this.name = name
        return this
    }

    fun getSequenceCount(): Int? {
        return sequenceCount
    }

    fun setSequenceCount(sequenceCount: Int?): PlaylistSearchViewModel {
        this.sequenceCount = sequenceCount
        return this
    }

    fun getDurationSeconds(): Int? {
        return durationSeconds
    }

    fun setDurationSeconds(durationSeconds: Int?): PlaylistSearchViewModel {
        this.durationSeconds = durationSeconds
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlaylistSearchViewModel

        if (id != other.id) return false
        if (name != other.name) return false
        if (sequenceCount != other.sequenceCount) return false
        if (durationSeconds != other.durationSeconds) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (sequenceCount?.hashCode() ?: 0)
        result = 31 * result + (durationSeconds?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "PlaylistSearchViewModel(id=$id, name=$name, sequenceCount=$sequenceCount, durationSeconds=$durationSeconds)"
    }
}
