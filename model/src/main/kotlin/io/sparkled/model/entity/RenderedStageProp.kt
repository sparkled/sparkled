package io.sparkled.model.entity

import javax.persistence.*
import java.util.Objects
import java.util.UUID

@Entity
@Table(name = "rendered_stage_prop")
class RenderedStageProp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private var id: Integer? = null

    @Column(name = "sequence_id")
    private var sequenceId: Integer? = null

    @Column(name = "stage_prop_uuid")
    private var stagePropUuid: UUID? = null

    @Column(name = "led_count")
    private var ledCount: Integer? = null

    @Lob
    @Column(name = "data")
    private var data: ByteArray? = null

    fun getId(): Integer {
        return id
    }

    fun setId(id: Integer): RenderedStageProp {
        this.id = id
        return this
    }

    fun getSequenceId(): Integer {
        return sequenceId
    }

    fun setSequenceId(sequenceId: Integer): RenderedStageProp {
        this.sequenceId = sequenceId
        return this
    }

    fun getStagePropUuid(): UUID {
        return stagePropUuid
    }

    fun setStagePropUuid(stagePropUuid: UUID): RenderedStageProp {
        this.stagePropUuid = stagePropUuid
        return this
    }

    fun getLedCount(): Integer {
        return ledCount
    }

    fun setLedCount(ledCount: Integer): RenderedStageProp {
        this.ledCount = ledCount
        return this
    }

    fun getData(): ByteArray {
        return data
    }

    fun setData(data: ByteArray): RenderedStageProp {
        this.data = data
        return this
    }

    @Override
    fun equals(o: Object?): Boolean {
        if (this === o) return true
        if (o == null || getClass() !== o!!.getClass()) return false
        val that = o
        return Objects.equals(id, that!!.id) &&
                Objects.equals(sequenceId, that.sequenceId) &&
                Objects.equals(stagePropUuid, that.stagePropUuid) &&
                Objects.equals(ledCount, that.ledCount)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(id, sequenceId, stagePropUuid, ledCount)
    }

    @Override
    fun toString(): String {
        return "RenderedStageProp{" +
                "id=" + id +
                ", sequenceId=" + sequenceId +
                ", stagePropUuid='" + stagePropUuid + '\'' +
                ", ledCount=" + ledCount +
                '}'
    }
}
