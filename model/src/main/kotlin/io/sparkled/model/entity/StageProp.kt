package io.sparkled.model.entity

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "stage_prop")
class StageProp {

    @Id
    @Column(name = "uuid")
    private var uuid: UUID? = null

    @Basic
    @Column(name = "stage_id")
    private var stageId: Int? = null

    @Basic
    @Column(name = "code")
    private var code: String? = null

    @Basic
    @Column(name = "name")
    private var name: String? = null

    @Basic
    @Column(name = "type")
    private var type: String? = null

    @Basic
    @Column(name = "led_count")
    private var ledCount: Int? = null

    @Basic
    @Column(name = "position_x")
    private var positionX: Int? = null

    @Basic
    @Column(name = "position_y")
    private var positionY: Int? = null

    @Basic
    @Column(name = "scale_x")
    private var scaleX: Float? = null

    @Basic
    @Column(name = "scale_y")
    private var scaleY: Float? = null

    @Basic
    @Column(name = "rotation")
    private var rotation: Int? = null

    @Basic
    @Column(name = "display_order")
    private var displayOrder: Int? = null

    fun getUuid(): UUID? {
        return uuid
    }

    fun setUuid(uuid: UUID?): StageProp {
        this.uuid = uuid
        return this
    }

    fun getStageId(): Int? {
        return stageId
    }

    fun setStageId(stageId: Int?): StageProp {
        this.stageId = stageId
        return this
    }

    fun getCode(): String? {
        return code
    }

    fun setCode(code: String?): StageProp {
        this.code = code
        return this
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?): StageProp {
        this.name = name
        return this
    }

    fun getType(): String? {
        return type
    }

    fun setType(type: String?): StageProp {
        this.type = type
        return this
    }

    fun getLedCount(): Int? {
        return ledCount
    }

    fun setLedCount(ledCount: Int?): StageProp {
        this.ledCount = ledCount
        return this
    }

    fun getPositionX(): Int? {
        return positionX
    }

    fun setPositionX(positionX: Int?): StageProp {
        this.positionX = positionX
        return this
    }

    fun getPositionY(): Int? {
        return positionY
    }

    fun setPositionY(positionY: Int?): StageProp {
        this.positionY = positionY
        return this
    }

    fun getScaleX(): Float? {
        return scaleX
    }

    fun setScaleX(scaleX: Float?): StageProp {
        this.scaleX = scaleX
        return this
    }

    fun getScaleY(): Float? {
        return scaleY
    }

    fun setScaleY(scaleY: Float?): StageProp {
        this.scaleY = scaleY
        return this
    }

    fun getRotation(): Int? {
        return rotation
    }

    fun setRotation(rotation: Int?): StageProp {
        this.rotation = rotation
        return this
    }

    fun getDisplayOrder(): Int? {
        return displayOrder
    }

    fun setDisplayOrder(displayOrder: Int?): StageProp {
        this.displayOrder = displayOrder
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StageProp

        if (uuid != other.uuid) return false
        if (stageId != other.stageId) return false
        if (code != other.code) return false
        if (name != other.name) return false
        if (type != other.type) return false
        if (ledCount != other.ledCount) return false
        if (positionX != other.positionX) return false
        if (positionY != other.positionY) return false
        if (scaleX != other.scaleX) return false
        if (scaleY != other.scaleY) return false
        if (rotation != other.rotation) return false
        if (displayOrder != other.displayOrder) return false

        return true
    }

    override fun hashCode(): Int {
        var result = uuid?.hashCode() ?: 0
        result = 31 * result + (stageId ?: 0)
        result = 31 * result + (code?.hashCode() ?: 0)
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + (ledCount ?: 0)
        result = 31 * result + (positionX ?: 0)
        result = 31 * result + (positionY ?: 0)
        result = 31 * result + (scaleX?.hashCode() ?: 0)
        result = 31 * result + (scaleY?.hashCode() ?: 0)
        result = 31 * result + (rotation ?: 0)
        result = 31 * result + (displayOrder ?: 0)
        return result
    }

    override fun toString(): String {
        return "StageProp(uuid=$uuid, stageId=$stageId, code=$code, name=$name, type=$type, ledCount=$ledCount, positionX=$positionX, positionY=$positionY, scaleX=$scaleX, scaleY=$scaleY, rotation=$rotation, displayOrder=$displayOrder)"
    }
}
