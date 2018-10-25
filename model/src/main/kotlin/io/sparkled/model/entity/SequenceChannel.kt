package io.sparkled.model.entity

import io.sparkled.model.animation.SequenceChannelEffects

import javax.persistence.*
import java.util.Objects
import java.util.UUID

@Entity
@Table(name = "sequence_channel")
class SequenceChannel {

    @Id
    @Column(name = "uuid")
    private var uuid: UUID? = null

    @Basic
    @Column(name = "sequence_id")
    private var sequenceId: Integer? = null

    @Basic
    @Column(name = "stage_prop_uuid")
    private var stagePropUuid: UUID? = null

    @Basic
    @Column(name = "name")
    private var name: String? = null

    @Basic
    @Column(name = "display_order")
    private var displayOrder: Integer? = null

    @Basic
    @Column(name = "channel_json", columnDefinition = "text")
    private var channelJson: String? = null

    fun getUuid(): UUID {
        return uuid
    }

    fun setUuid(uuid: UUID): SequenceChannel {
        this.uuid = uuid
        return this
    }

    fun getSequenceId(): Integer {
        return sequenceId
    }

    fun setSequenceId(sequenceId: Integer): SequenceChannel {
        this.sequenceId = sequenceId
        return this
    }

    fun getStagePropUuid(): UUID {
        return stagePropUuid
    }

    fun setStagePropUuid(stagePropUuid: UUID): SequenceChannel {
        this.stagePropUuid = stagePropUuid
        return this
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String): SequenceChannel {
        this.name = name
        return this
    }

    fun getDisplayOrder(): Integer {
        return displayOrder
    }

    fun setDisplayOrder(displayOrder: Integer): SequenceChannel {
        this.displayOrder = displayOrder
        return this
    }

    /**
     * @return a [SequenceChannelEffects] instance in JSON form
     */
    fun getChannelJson(): String {
        return channelJson
    }

    /**
     * @param channelJson a [SequenceChannelEffects] instance in JSON form
     */
    fun setChannelJson(channelJson: String): SequenceChannel {
        this.channelJson = channelJson
        return this
    }

    @Override
    fun equals(o: Object?): Boolean {
        if (this === o) return true
        if (o == null || getClass() !== o!!.getClass()) return false
        val that = o
        return Objects.equals(uuid, that!!.uuid) &&
                Objects.equals(sequenceId, that.sequenceId) &&
                Objects.equals(stagePropUuid, that.stagePropUuid) &&
                Objects.equals(name, that.name) &&
                Objects.equals(displayOrder, that.displayOrder) &&
                Objects.equals(channelJson, that.channelJson)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(uuid, sequenceId, stagePropUuid, name, displayOrder, channelJson)
    }

    @Override
    fun toString(): String {
        return "SequenceChannel{" +
                "uuid=" + uuid +
                ", sequenceId=" + sequenceId +
                ", stagePropUuid=" + stagePropUuid +
                ", name='" + name + '\'' +
                ", displayOrder=" + displayOrder +
                ", channelJson='" + channelJson + '\'' +
                '}'
    }
}
