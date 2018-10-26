package io.sparkled.viewmodel.playlist.sequence

import io.sparkled.viewmodel.ViewModel
import java.util.UUID

class PlaylistSequenceViewModel : ViewModel {

    private var uuid: UUID? = null
    private var sequenceId: Int? = null
    private var displayOrder: Int? = null

    fun getUuid(): UUID? {
        return uuid
    }

    fun setUuid(uuid: UUID?): PlaylistSequenceViewModel {
        this.uuid = uuid
        return this
    }

    fun getSequenceId(): Int? {
        return sequenceId
    }

    fun setSequenceId(sequenceId: Int?): PlaylistSequenceViewModel {
        this.sequenceId = sequenceId
        return this
    }

    fun getDisplayOrder(): Int? {
        return displayOrder
    }

    fun setDisplayOrder(displayOrder: Int?): PlaylistSequenceViewModel {
        this.displayOrder = displayOrder
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlaylistSequenceViewModel

        if (uuid != other.uuid) return false
        if (sequenceId != other.sequenceId) return false
        if (displayOrder != other.displayOrder) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uuid?.hashCode() ?: 0
        result = 31 * result + (sequenceId ?: 0)
        result = 31 * result + (displayOrder ?: 0)
        return result
    }

    override fun toString(): String {
        return "PlaylistSequenceViewModel(uuid=$uuid, sequenceId=$sequenceId, displayOrder=$displayOrder)"
    }
}
