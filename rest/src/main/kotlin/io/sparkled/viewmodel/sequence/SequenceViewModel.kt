package io.sparkled.viewmodel.sequence

import io.sparkled.model.entity.SequenceStatus
import io.sparkled.viewmodel.ViewModel
import io.sparkled.viewmodel.sequence.channel.SequenceChannelViewModel
import java.util.*

class SequenceViewModel : ViewModel {

    private var id: Int? = null
    private var songId: Int? = null
    private var stageId: Int? = null
    private var name: String? = null
    private var framesPerSecond: Int? = null
    private var frameCount: Int? = null
    private var status: SequenceStatus? = null
    private var channels: List<SequenceChannelViewModel> = ArrayList()

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?): SequenceViewModel {
        this.id = id
        return this
    }

    fun getSongId(): Int? {
        return songId
    }

    fun setSongId(songId: Int?): SequenceViewModel {
        this.songId = songId
        return this
    }

    fun getStageId(): Int? {
        return stageId
    }

    fun setStageId(stageId: Int?): SequenceViewModel {
        this.stageId = stageId
        return this
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?): SequenceViewModel {
        this.name = name
        return this
    }

    fun getFramesPerSecond(): Int? {
        return framesPerSecond
    }

    fun setFramesPerSecond(framesPerSecond: Int?): SequenceViewModel {
        this.framesPerSecond = framesPerSecond
        return this
    }

    fun getFrameCount(): Int? {
        return frameCount
    }

    fun setFrameCount(frameCount: Int?): SequenceViewModel {
        this.frameCount = frameCount
        return this
    }

    fun getStatus(): SequenceStatus? {
        return status
    }

    fun setStatus(status: SequenceStatus?): SequenceViewModel {
        this.status = status
        return this
    }

    fun getChannels(): List<SequenceChannelViewModel> {
        return channels
    }

    fun setChannels(channels: List<SequenceChannelViewModel>): SequenceViewModel {
        this.channels = channels
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SequenceViewModel

        if (id != other.id) return false
        if (songId != other.songId) return false
        if (stageId != other.stageId) return false
        if (name != other.name) return false
        if (framesPerSecond != other.framesPerSecond) return false
        if (frameCount != other.frameCount) return false
        if (status != other.status) return false
        if (channels != other.channels) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (songId ?: 0)
        result = 31 * result + (stageId ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (framesPerSecond ?: 0)
        result = 31 * result + (frameCount ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        result = 31 * result + channels.hashCode()
        return result
    }

    override fun toString(): String {
        return "SequenceViewModel(id=$id, songId=$songId, stageId=$stageId, name=$name, framesPerSecond=$framesPerSecond, frameCount=$frameCount, status=$status, channels=$channels)"
    }
}
