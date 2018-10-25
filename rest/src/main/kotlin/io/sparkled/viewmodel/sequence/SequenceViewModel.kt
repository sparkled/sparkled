package io.sparkled.viewmodel.sequence

import io.sparkled.model.entity.SequenceStatus
import io.sparkled.viewmodel.ViewModel
import io.sparkled.viewmodel.sequence.channel.SequenceChannelViewModel

import java.util.ArrayList
import java.util.Objects

class SequenceViewModel : ViewModel {

    private var id: Integer? = null
    private var songId: Integer? = null
    private var stageId: Integer? = null
    private var name: String? = null
    private var framesPerSecond: Integer? = null
    private var frameCount: Integer? = null
    private var status: SequenceStatus? = null
    private var channels: List<SequenceChannelViewModel> = ArrayList()

    fun getId(): Integer {
        return id
    }

    fun setId(id: Integer): SequenceViewModel {
        this.id = id
        return this
    }

    fun getSongId(): Integer {
        return songId
    }

    fun setSongId(songId: Integer): SequenceViewModel {
        this.songId = songId
        return this
    }

    fun getStageId(): Integer {
        return stageId
    }

    fun setStageId(stageId: Integer): SequenceViewModel {
        this.stageId = stageId
        return this
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String): SequenceViewModel {
        this.name = name
        return this
    }

    fun getFramesPerSecond(): Integer {
        return framesPerSecond
    }

    fun setFramesPerSecond(framesPerSecond: Integer): SequenceViewModel {
        this.framesPerSecond = framesPerSecond
        return this
    }

    fun getFrameCount(): Integer {
        return frameCount
    }

    fun setFrameCount(frameCount: Integer): SequenceViewModel {
        this.frameCount = frameCount
        return this
    }

    fun getStatus(): SequenceStatus {
        return status
    }

    fun setStatus(status: SequenceStatus): SequenceViewModel {
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

    @Override
    fun equals(o: Object?): Boolean {
        if (this === o) return true
        if (o == null || getClass() !== o!!.getClass()) return false
        val that = o
        return Objects.equals(id, that!!.id) &&
                Objects.equals(songId, that.songId) &&
                Objects.equals(stageId, that.stageId) &&
                Objects.equals(name, that.name) &&
                Objects.equals(framesPerSecond, that.framesPerSecond) &&
                Objects.equals(frameCount, that.frameCount) &&
                status === that.status &&
                Objects.equals(channels, that.channels)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(id, songId, stageId, name, framesPerSecond, frameCount, status, channels)
    }

    @Override
    fun toString(): String {
        return "SongViewModel{" +
                "id=" + id +
                ", songId=" + songId +
                ", stageId=" + stageId +
                ", name='" + name + '\'' +
                ", framesPerSecond=" + framesPerSecond +
                ", frameCount=" + frameCount +
                ", status=" + status +
                ", channels=" + channels +
                '}'
    }
}
