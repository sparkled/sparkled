package io.sparkled.viewmodel.sequence.channel

import io.sparkled.model.animation.effect.Effect
import io.sparkled.viewmodel.ViewModel
import java.util.*

class SequenceChannelViewModel : ViewModel {

    private var uuid: UUID? = null
    private var sequenceId: Int? = null
    private var stagePropUuid: UUID? = null
    private var name: String? = null
    private var displayOrder: Int? = null
    private var effects: List<Effect> = ArrayList()

    fun getUuid(): UUID? {
        return uuid
    }

    fun setUuid(uuid: UUID?): SequenceChannelViewModel {
        this.uuid = uuid
        return this
    }

    fun getSequenceId(): Int? {
        return sequenceId
    }

    fun setSequenceId(sequenceId: Int?): SequenceChannelViewModel {
        this.sequenceId = sequenceId
        return this
    }

    fun getStagePropUuid(): UUID? {
        return stagePropUuid
    }

    fun setStagePropUuid(stagePropUuid: UUID?): SequenceChannelViewModel {
        this.stagePropUuid = stagePropUuid
        return this
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?): SequenceChannelViewModel {
        this.name = name
        return this
    }

    fun getDisplayOrder(): Int? {
        return displayOrder
    }

    fun setDisplayOrder(displayOrder: Int?): SequenceChannelViewModel {
        this.displayOrder = displayOrder
        return this
    }

    fun getEffects(): List<Effect> {
        return effects
    }

    fun setEffects(effects: List<Effect>): SequenceChannelViewModel {
        this.effects = effects
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SequenceChannelViewModel

        if (uuid != other.uuid) return false
        if (sequenceId != other.sequenceId) return false
        if (stagePropUuid != other.stagePropUuid) return false
        if (name != other.name) return false
        if (displayOrder != other.displayOrder) return false
        if (effects != other.effects) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uuid?.hashCode() ?: 0
        result = 31 * result + (sequenceId?.hashCode() ?: 0)
        result = 31 * result + (stagePropUuid?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (displayOrder?.hashCode() ?: 0)
        result = 31 * result + effects.hashCode()
        return result
    }

    override fun toString(): String {
        return "SequenceChannelViewModel(uuid=$uuid, sequenceId=$sequenceId, stagePropUuid=$stagePropUuid, name=$name, displayOrder=$displayOrder, effects=$effects)"
    }
}
