package io.sparkled.viewmodel.sequence.search

import io.sparkled.model.entity.SequenceStatus
import io.sparkled.viewmodel.ViewModel

class SequenceSearchViewModel : ViewModel {
    private var id: Int? = null
    private var name: String? = null
    private var songName: String? = null
    private var stageName: String? = null
    private var framesPerSecond: Int? = null
    private var durationSeconds: Int? = null
    private var status: SequenceStatus? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?): SequenceSearchViewModel {
        this.id = id
        return this
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?): SequenceSearchViewModel {
        this.name = name
        return this
    }

    fun getSongName(): String? {
        return songName
    }

    fun setSongName(songName: String?): SequenceSearchViewModel {
        this.songName = songName
        return this
    }

    fun getStageName(): String? {
        return stageName
    }

    fun setStageName(stageName: String?): SequenceSearchViewModel {
        this.stageName = stageName
        return this
    }

    fun getFramesPerSecond(): Int? {
        return framesPerSecond
    }

    fun setFramesPerSecond(framesPerSecond: Int?): SequenceSearchViewModel {
        this.framesPerSecond = framesPerSecond
        return this
    }

    fun getDurationSeconds(): Int? {
        return durationSeconds
    }

    fun setDurationSeconds(durationSeconds: Int?): SequenceSearchViewModel {
        this.durationSeconds = durationSeconds
        return this
    }

    fun getStatus(): SequenceStatus? {
        return status
    }

    fun setStatus(status: SequenceStatus?): SequenceSearchViewModel {
        this.status = status
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SequenceSearchViewModel

        if (id != other.id) return false
        if (name != other.name) return false
        if (songName != other.songName) return false
        if (stageName != other.stageName) return false
        if (framesPerSecond != other.framesPerSecond) return false
        if (durationSeconds != other.durationSeconds) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (songName?.hashCode() ?: 0)
        result = 31 * result + (stageName?.hashCode() ?: 0)
        result = 31 * result + (framesPerSecond ?: 0)
        result = 31 * result + (durationSeconds ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "SequenceSearchViewModel(id=$id, name=$name, songName=$songName, stageName=$stageName, framesPerSecond=$framesPerSecond, durationSeconds=$durationSeconds, status=$status)"
    }
}
