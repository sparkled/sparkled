package io.sparkled.model.entity

import java.util.UUID
import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "playlist_sequence")
class PlaylistSequence {

    @Id
    @Column(name = "uuid", nullable = false)
    private var uuid: UUID? = null

    @Basic
    @Column(name = "playlist_id", nullable = false)
    private var playlistId: Int? = null

    @Basic
    @Column(name = "sequence_id", nullable = false)
    private var sequenceId: Int? = null

    @Basic
    @Column(name = "display_order", nullable = false)
    private var displayOrder: Int? = null

    fun getUuid(): UUID? {
        return uuid
    }

    fun setUuid(uuid: UUID?): PlaylistSequence {
        this.uuid = uuid
        return this
    }

    fun getPlaylistId(): Int? {
        return playlistId
    }

    fun setPlaylistId(playlistId: Int?): PlaylistSequence {
        this.playlistId = playlistId
        return this
    }

    fun getSequenceId(): Int? {
        return sequenceId
    }

    fun setSequenceId(sequenceId: Int?): PlaylistSequence {
        this.sequenceId = sequenceId
        return this
    }

    fun getDisplayOrder(): Int? {
        return displayOrder
    }

    fun setDisplayOrder(displayOrder: Int?): PlaylistSequence {
        this.displayOrder = displayOrder
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlaylistSequence

        if (uuid != other.uuid) return false
        if (playlistId != other.playlistId) return false
        if (sequenceId != other.sequenceId) return false
        if (displayOrder != other.displayOrder) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uuid?.hashCode() ?: 0
        result = 31 * result + (playlistId ?: 0)
        result = 31 * result + (sequenceId ?: 0)
        result = 31 * result + (displayOrder ?: 0)
        return result
    }

    override fun toString(): String {
        return "PlaylistSequence(uuid=$uuid, playlistId=$playlistId, sequenceId=$sequenceId, displayOrder=$displayOrder)"
    }
}
