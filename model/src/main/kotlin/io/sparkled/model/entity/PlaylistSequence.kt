package io.sparkled.model.entity

import javax.persistence.*
import java.util.Objects
import java.util.UUID

@Entity
@Table(name = "playlist_sequence")
class PlaylistSequence {

    @Id
    @Column(name = "uuid", nullable = false)
    private var uuid: UUID? = null

    @Basic
    @Column(name = "playlist_id", nullable = false)
    private var playlistId: Integer? = null

    @Basic
    @Column(name = "sequence_id", nullable = false)
    private var sequenceId: Integer? = null

    @Basic
    @Column(name = "display_order", nullable = false)
    private var displayOrder: Integer? = null

    fun getUuid(): UUID {
        return uuid
    }

    fun setUuid(uuid: UUID): PlaylistSequence {
        this.uuid = uuid
        return this
    }

    fun getPlaylistId(): Integer {
        return playlistId
    }

    fun setPlaylistId(playlistId: Integer): PlaylistSequence {
        this.playlistId = playlistId
        return this
    }

    fun getSequenceId(): Integer {
        return sequenceId
    }

    fun setSequenceId(sequenceId: Integer): PlaylistSequence {
        this.sequenceId = sequenceId
        return this
    }

    fun getDisplayOrder(): Integer {
        return displayOrder
    }

    fun setDisplayOrder(displayOrder: Integer): PlaylistSequence {
        this.displayOrder = displayOrder
        return this
    }

    @Override
    fun equals(o: Object?): Boolean {
        if (this === o) return true
        if (o == null || getClass() !== o!!.getClass()) return false
        val that = o
        return Objects.equals(uuid, that!!.uuid) &&
                Objects.equals(playlistId, that.playlistId) &&
                Objects.equals(sequenceId, that.sequenceId) &&
                Objects.equals(displayOrder, that.displayOrder)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(uuid, playlistId, sequenceId, displayOrder)
    }

    @Override
    fun toString(): String {
        return "PlaylistSequence{" +
                "id=" + uuid +
                ", playlistId=" + playlistId +
                ", sequenceId=" + sequenceId +
                ", displayOrder=" + displayOrder +
                '}'
    }
}
