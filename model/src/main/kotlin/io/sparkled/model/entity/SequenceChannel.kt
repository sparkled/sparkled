package io.sparkled.model.entity

import io.sparkled.model.animation.SequenceChannelEffects
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "sequence_channel")
class SequenceChannel {

    @Id
    @Column(name = "uuid")
    private var uuid: UUID? = null

    @Basic
    @Column(name = "sequence_id")
    private var sequenceId: Int? = null

    @Basic
    @Column(name = "stage_prop_uuid")
    private var stagePropUuid: UUID? = null

    @Basic
    @Column(name = "name")
    private var name: String? = null

    @Basic
    @Column(name = "display_order")
    private var displayOrder: Int? = null

    @Basic
    @Column(name = "channel_json", columnDefinition = "text")
    private var channelJson: String? = null

    fun getUuid(): UUID? {
        return uuid
    }

    fun setUuid(uuid: UUID?): SequenceChannel {
        this.uuid = uuid
        return this
    }

    fun getSequenceId(): Int? {
        return sequenceId
    }

    fun setSequenceId(sequenceId: Int?): SequenceChannel {
        this.sequenceId = sequenceId
        return this
    }

    fun getStagePropUuid(): UUID? {
        return stagePropUuid
    }

    fun setStagePropUuid(stagePropUuid: UUID?): SequenceChannel {
        this.stagePropUuid = stagePropUuid
        return this
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?): SequenceChannel {
        this.name = name
        return this
    }

    fun getDisplayOrder(): Int? {
        return displayOrder
    }

    fun setDisplayOrder(displayOrder: Int?): SequenceChannel {
        this.displayOrder = displayOrder
        return this
    }

    /**
     * @return a [SequenceChannelEffects] instance in JSON form
     */
    fun getChannelJson(): String? {
        return channelJson
    }

    /**
     * @param channelJson a [SequenceChannelEffects] instance in JSON form
     */
    fun setChannelJson(channelJson: String?): SequenceChannel {
        this.channelJson = channelJson
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SequenceChannel

        if (uuid != other.uuid) return false
        if (sequenceId != other.sequenceId) return false
        if (stagePropUuid != other.stagePropUuid) return false
        if (name != other.name) return false
        if (displayOrder != other.displayOrder) return false
        if (channelJson != other.channelJson) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uuid?.hashCode() ?: 0
        result = 31 * result + (sequenceId ?: 0)
        result = 31 * result + (stagePropUuid?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (displayOrder ?: 0)
        result = 31 * result + (channelJson?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "SequenceChannel(uuid=$uuid, sequenceId=$sequenceId, stagePropUuid=$stagePropUuid, name=$name, displayOrder=$displayOrder, channelJson=$channelJson)"
    }
}
