package io.sparkled.model.entity

import javax.persistence.*
import java.util.Objects

@Entity
@Table(name = "sequence")
class Sequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private var id: Integer? = null

    @Basic
    @Column(name = "song_id")
    private var songId: Integer? = null

    @Basic
    @Column(name = "stage_id")
    private var stageId: Integer? = null

    @Basic
    @Column(name = "name")
    private var name: String? = null

    @Basic
    @Column(name = "frames_per_second")
    private var framesPerSecond: Integer? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private var status: SequenceStatus? = null

    fun getId(): Integer {
        return id
    }

    fun setId(id: Integer): Sequence {
        this.id = id
        return this
    }

    fun getSongId(): Integer {
        return songId
    }

    fun setSongId(songId: Integer): Sequence {
        this.songId = songId
        return this
    }

    fun getStageId(): Integer {
        return stageId
    }

    fun setStageId(stageId: Integer): Sequence {
        this.stageId = stageId
        return this
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String): Sequence {
        this.name = name
        return this
    }

    fun getFramesPerSecond(): Integer {
        return framesPerSecond
    }

    fun setFramesPerSecond(framesPerSecond: Integer): Sequence {
        this.framesPerSecond = framesPerSecond
        return this
    }

    fun getStatus(): SequenceStatus {
        return status
    }

    fun setStatus(status: SequenceStatus): Sequence {
        this.status = status
        return this
    }

    @Override
    fun equals(o: Object?): Boolean {
        if (this === o) return true
        if (o == null || getClass() !== o!!.getClass()) return false
        val sequence = o
        return Objects.equals(id, sequence!!.id) &&
                Objects.equals(songId, sequence.songId) &&
                Objects.equals(stageId, sequence.stageId) &&
                Objects.equals(name, sequence.name) &&
                Objects.equals(framesPerSecond, sequence.framesPerSecond) &&
                status === sequence.status
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(id, songId, stageId, name, framesPerSecond, status)
    }

    @Override
    fun toString(): String {
        return "Sequence{" +
                "id=" + id +
                ", songId=" + songId +
                ", stageId=" + stageId +
                ", name='" + name + '\'' +
                ", framesPerSecond=" + framesPerSecond +
                ", status=" + status +
                '}'
    }
}
