package io.sparkled.viewmodel.playlist.sequence

import io.sparkled.viewmodel.ViewModel

import java.util.Objects
import java.util.UUID

class PlaylistSequenceViewModel : ViewModel {

    private var uuid: UUID? = null
    private var sequenceId: Integer? = null
    private var displayOrder: Integer? = null

    fun getUuid(): UUID {
        return uuid
    }

    fun setUuid(uuid: UUID): PlaylistSequenceViewModel {
        this.uuid = uuid
        return this
    }

    fun getSequenceId(): Integer {
        return sequenceId
    }

    fun setSequenceId(sequenceId: Integer): PlaylistSequenceViewModel {
        this.sequenceId = sequenceId
        return this
    }

    fun getDisplayOrder(): Integer {
        return displayOrder
    }

    fun setDisplayOrder(displayOrder: Integer): PlaylistSequenceViewModel {
        this.displayOrder = displayOrder
        return this
    }

    @Override
    fun equals(o: Object?): Boolean {
        if (this === o) return true
        if (o == null || getClass() !== o!!.getClass()) return false
        val that = o
        return Objects.equals(uuid, that!!.uuid) &&
                Objects.equals(sequenceId, that.sequenceId) &&
                Objects.equals(displayOrder, that.displayOrder)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(uuid, sequenceId, displayOrder)
    }

    @Override
    fun toString(): String {
        return "PlaylistSequenceViewModel{" +
                "uuid=" + uuid +
                ", sequenceId=" + sequenceId +
                ", displayOrder=" + displayOrder +
                '}'
    }
}
