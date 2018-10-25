package io.sparkled.viewmodel.sequence.channel

import io.sparkled.model.animation.effect.Effect
import io.sparkled.viewmodel.ViewModel

import java.util.ArrayList
import java.util.Objects
import java.util.UUID

class SequenceChannelViewModel : ViewModel {

    private var uuid: UUID? = null
    private var sequenceId: Integer? = null
    private var stagePropUuid: UUID? = null
    private var name: String? = null
    private var displayOrder: Integer? = null
    private var effects: List<Effect> = ArrayList()

    fun getUuid(): UUID {
        return uuid
    }

    fun setUuid(uuid: UUID): SequenceChannelViewModel {
        this.uuid = uuid
        return this
    }

    fun getSequenceId(): Integer {
        return sequenceId
    }

    fun setSequenceId(sequenceId: Integer): SequenceChannelViewModel {
        this.sequenceId = sequenceId
        return this
    }

    fun getStagePropUuid(): UUID {
        return stagePropUuid
    }

    fun setStagePropUuid(stagePropUuid: UUID): SequenceChannelViewModel {
        this.stagePropUuid = stagePropUuid
        return this
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String): SequenceChannelViewModel {
        this.name = name
        return this
    }

    fun getDisplayOrder(): Integer {
        return displayOrder
    }

    fun setDisplayOrder(displayOrder: Integer): SequenceChannelViewModel {
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

    @Override
    fun equals(o: Object): Boolean {
        if (this === o) return true
        if (o !is SequenceChannelViewModel) return false
        val that = o
        return Objects.equals(sequenceId, that.sequenceId) &&
                Objects.equals(uuid, that.uuid) &&
                Objects.equals(stagePropUuid, that.stagePropUuid) &&
                Objects.equals(name, that.name) &&
                Objects.equals(displayOrder, that.displayOrder) &&
                Objects.equals(effects, that.effects)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(uuid, sequenceId, stagePropUuid, name, displayOrder, effects)
    }
}
