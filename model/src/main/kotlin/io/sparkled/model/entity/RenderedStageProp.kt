package io.sparkled.model.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "rendered_stage_prop")
class RenderedStageProp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private var id: Int? = null

    @Column(name = "sequence_id")
    private var sequenceId: Int? = null

    @Column(name = "stage_prop_uuid")
    private var stagePropUuid: UUID? = null

    @Column(name = "led_count")
    private var ledCount: Int? = null

    @Lob
    @Column(name = "data")
    private var data: ByteArray? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?): RenderedStageProp {
        this.id = id
        return this
    }

    fun getSequenceId(): Int? {
        return sequenceId
    }

    fun setSequenceId(sequenceId: Int?): RenderedStageProp {
        this.sequenceId = sequenceId
        return this
    }

    fun getStagePropUuid(): UUID? {
        return stagePropUuid
    }

    fun setStagePropUuid(stagePropUuid: UUID?): RenderedStageProp {
        this.stagePropUuid = stagePropUuid
        return this
    }

    fun getLedCount(): Int? {
        return ledCount
    }

    fun setLedCount(ledCount: Int?): RenderedStageProp {
        this.ledCount = ledCount
        return this
    }

    fun getData(): ByteArray? {
        return data
    }

    fun setData(data: ByteArray?): RenderedStageProp {
        this.data = data
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as RenderedStageProp

        if (id != other.id) return false
        if (sequenceId != other.sequenceId) return false
        if (stagePropUuid != other.stagePropUuid) return false
        if (ledCount != other.ledCount) return false
        if (!Arrays.equals(data, other.data)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (sequenceId ?: 0)
        result = 31 * result + (stagePropUuid?.hashCode() ?: 0)
        result = 31 * result + (ledCount ?: 0)
        return result
    }

    override fun toString(): String {
        return "RenderedStageProp(id=$id, sequenceId=$sequenceId, stagePropUuid=$stagePropUuid, ledCount=$ledCount)"
    }
}
