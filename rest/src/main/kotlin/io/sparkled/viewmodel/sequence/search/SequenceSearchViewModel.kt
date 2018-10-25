package io.sparkled.viewmodel.sequence.search

import io.sparkled.model.entity.SequenceStatus
import io.sparkled.viewmodel.ViewModel

import java.util.Objects

class SequenceSearchViewModel : ViewModel {
    private var id: Integer? = null
    private var name: String? = null
    private var songName: String? = null
    private var stageName: String? = null
    private var framesPerSecond: Integer? = null
    private var durationSeconds: Integer? = null
    private var status: SequenceStatus? = null

    fun getId(): Integer {
        return id
    }

    fun setId(id: Integer): SequenceSearchViewModel {
        this.id = id
        return this
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String): SequenceSearchViewModel {
        this.name = name
        return this
    }

    fun getSongName(): String {
        return songName
    }

    fun setSongName(songName: String): SequenceSearchViewModel {
        this.songName = songName
        return this
    }

    fun getStageName(): String {
        return stageName
    }

    fun setStageName(stageName: String): SequenceSearchViewModel {
        this.stageName = stageName
        return this
    }

    fun getFramesPerSecond(): Integer {
        return framesPerSecond
    }

    fun setFramesPerSecond(framesPerSecond: Integer): SequenceSearchViewModel {
        this.framesPerSecond = framesPerSecond
        return this
    }

    fun getDurationSeconds(): Integer {
        return durationSeconds
    }

    fun setDurationSeconds(durationSeconds: Integer): SequenceSearchViewModel {
        this.durationSeconds = durationSeconds
        return this
    }

    fun getStatus(): SequenceStatus {
        return status
    }

    fun setStatus(status: SequenceStatus): SequenceSearchViewModel {
        this.status = status
        return this
    }

    @Override
    fun equals(o: Object?): Boolean {
        if (this === o) return true
        if (o == null || getClass() !== o!!.getClass()) return false
        val that = o
        return Objects.equals(id, that!!.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(songName, that.songName) &&
                Objects.equals(stageName, that.stageName) &&
                Objects.equals(framesPerSecond, that.framesPerSecond) &&
                Objects.equals(durationSeconds, that.durationSeconds) &&
                status === that.status
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(id, name, songName, stageName, framesPerSecond, durationSeconds, status)
    }

    @Override
    fun toString(): String {
        return "SequenceSearchViewModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", songName='" + songName + '\'' +
                ", stageName='" + stageName + '\'' +
                ", framesPerSecond=" + framesPerSecond +
                ", durationSeconds=" + durationSeconds +
                ", status=" + status +
                '}'
    }
}
