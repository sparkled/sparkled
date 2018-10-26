package io.sparkled.model.entity

import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "sequence")
class Sequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private var id: Int? = null

    @Basic
    @Column(name = "song_id")
    private var songId: Int? = null

    @Basic
    @Column(name = "stage_id")
    private var stageId: Int? = null

    @Basic
    @Column(name = "name")
    private var name: String? = null

    @Basic
    @Column(name = "frames_per_second")
    private var framesPerSecond: Int? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private var status: SequenceStatus? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?): Sequence {
        this.id = id
        return this
    }

    fun getSongId(): Int? {
        return songId
    }

    fun setSongId(songId: Int?): Sequence {
        this.songId = songId
        return this
    }

    fun getStageId(): Int? {
        return stageId
    }

    fun setStageId(stageId: Int?): Sequence {
        this.stageId = stageId
        return this
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?): Sequence {
        this.name = name
        return this
    }

    fun getFramesPerSecond(): Int? {
        return framesPerSecond
    }

    fun setFramesPerSecond(framesPerSecond: Int?): Sequence {
        this.framesPerSecond = framesPerSecond
        return this
    }

    fun getStatus(): SequenceStatus? {
        return status
    }

    fun setStatus(status: SequenceStatus?): Sequence {
        this.status = status
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Sequence

        if (id != other.id) return false
        if (songId != other.songId) return false
        if (stageId != other.stageId) return false
        if (name != other.name) return false
        if (framesPerSecond != other.framesPerSecond) return false
        if (status != other.status) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (songId ?: 0)
        result = 31 * result + (stageId ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (framesPerSecond ?: 0)
        result = 31 * result + (status?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Sequence(id=$id, songId=$songId, stageId=$stageId, name=$name, framesPerSecond=$framesPerSecond, status=$status)"
    }
}
