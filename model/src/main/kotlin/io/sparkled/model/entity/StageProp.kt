package io.sparkled.model.entity

import javax.persistence.*
import java.util.Objects
import java.util.UUID

@Entity
@Table(name = "stage_prop")
class StageProp {

    @Id
    @Column(name = "uuid")
    private var uuid: UUID? = null

    @Basic
    @Column(name = "stage_id")
    private var stageId: Integer? = null

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
    private var ledCount: Integer = 0

    @Basic
    @Column(name = "position_x")
    private var positionX: Integer = 0

    @Basic
    @Column(name = "position_y")
    private var positionY: Integer = 0

    @Basic
    @Column(name = "scale_x")
    private var scaleX = 1f

    @Basic
    @Column(name = "scale_y")
    private var scaleY = 1f

    @Basic
    @Column(name = "rotation")
    private var rotation: Integer = 0

    @Basic
    @Column(name = "display_order")
    private var displayOrder: Integer = 0

    fun getUuid(): UUID {
        return uuid
    }

    fun setUuid(uuid: UUID): StageProp {
        this.uuid = uuid
        return this
    }

    fun getStageId(): Integer {
        return stageId
    }

    fun setStageId(stageId: Integer): StageProp {
        this.stageId = stageId
        return this
    }

    fun getCode(): String {
        return code
    }

    fun setCode(code: String): StageProp {
        this.code = code
        return this
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String): StageProp {
        this.name = name
        return this
    }

    fun getType(): String {
        return type
    }

    fun setType(type: String): StageProp {
        this.type = type
        return this
    }

    fun getLedCount(): Integer {
        return ledCount
    }

    fun setLedCount(ledCount: Integer): StageProp {
        this.ledCount = ledCount
        return this
    }

    fun getPositionX(): Integer {
        return positionX
    }

    fun setPositionX(positionX: Integer): StageProp {
        this.positionX = positionX
        return this
    }

    fun getPositionY(): Integer {
        return positionY
    }

    fun setPositionY(positionY: Integer): StageProp {
        this.positionY = positionY
        return this
    }

    fun getScaleX(): Float {
        return scaleX
    }

    fun setScaleX(scaleX: Float): StageProp {
        this.scaleX = scaleX
        return this
    }

    fun getScaleY(): Float {
        return scaleY
    }

    fun setScaleY(scaleY: Float): StageProp {
        this.scaleY = scaleY
        return this
    }

    fun getRotation(): Integer {
        return rotation
    }

    fun setRotation(rotation: Integer): StageProp {
        this.rotation = rotation
        return this
    }

    fun getDisplayOrder(): Integer {
        return displayOrder
    }

    fun setDisplayOrder(displayOrder: Integer): StageProp {
        this.displayOrder = displayOrder
        return this
    }

    @Override
    fun equals(o: Object?): Boolean {
        if (this === o) return true
        if (o == null || getClass() !== o!!.getClass()) return false
        val stageProp = o
        return Float.compare(stageProp!!.scaleX, scaleX) === 0 &&
                Float.compare(stageProp!!.scaleY, scaleY) === 0 &&
                Objects.equals(uuid, stageProp!!.uuid) &&
                Objects.equals(stageId, stageProp.stageId) &&
                Objects.equals(code, stageProp.code) &&
                Objects.equals(name, stageProp.name) &&
                Objects.equals(type, stageProp.type) &&
                Objects.equals(ledCount, stageProp.ledCount) &&
                Objects.equals(positionX, stageProp.positionX) &&
                Objects.equals(positionY, stageProp.positionY) &&
                Objects.equals(rotation, stageProp.rotation) &&
                Objects.equals(displayOrder, stageProp.displayOrder)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(uuid, stageId, code, name, type, ledCount, positionX, positionY, scaleX, scaleY, rotation, displayOrder)
    }

    @Override
    fun toString(): String {
        return "StageProp{" +
                "uuid=" + uuid +
                ", stageId=" + stageId +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", ledCount=" + ledCount +
                ", positionX=" + positionX +
                ", positionY=" + positionY +
                ", scaleX=" + scaleX +
                ", scaleY=" + scaleY +
                ", rotation=" + rotation +
                ", displayOrder=" + displayOrder +
                '}'
    }
}
