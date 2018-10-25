package io.sparkled.viewmodel.stage.prop

import io.sparkled.viewmodel.ViewModel

import java.util.Objects
import java.util.UUID

class StagePropViewModel : ViewModel {

    private var uuid: UUID? = null
    private var stageId: Integer? = null
    private var code: String? = null
    private var name: String? = null
    private var type: String? = null
    private var ledCount: Integer = 0
    private var positionX: Integer = 0
    private var positionY: Integer = 0
    private var scaleX = 1f
    private var scaleY = 1f
    private var rotation: Integer = 0
    private var displayOrder: Integer = 0

    fun getUuid(): UUID {
        return uuid
    }

    fun setUuid(uuid: UUID): StagePropViewModel {
        this.uuid = uuid
        return this
    }

    fun getStageId(): Integer {
        return stageId
    }

    fun setStageId(stageId: Integer): StagePropViewModel {
        this.stageId = stageId
        return this
    }

    fun getCode(): String {
        return code
    }

    fun setCode(code: String): StagePropViewModel {
        this.code = code
        return this
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String): StagePropViewModel {
        this.name = name
        return this
    }

    fun getType(): String {
        return type
    }

    fun setType(type: String): StagePropViewModel {
        this.type = type
        return this
    }

    fun getLedCount(): Integer {
        return ledCount
    }

    fun setLedCount(ledCount: Integer): StagePropViewModel {
        this.ledCount = ledCount
        return this
    }

    fun getPositionX(): Integer {
        return positionX
    }

    fun setPositionX(positionX: Integer): StagePropViewModel {
        this.positionX = positionX
        return this
    }

    fun getPositionY(): Integer {
        return positionY
    }

    fun setPositionY(positionY: Integer): StagePropViewModel {
        this.positionY = positionY
        return this
    }

    fun getScaleX(): Float {
        return scaleX
    }

    fun setScaleX(scaleX: Float): StagePropViewModel {
        this.scaleX = scaleX
        return this
    }

    fun getScaleY(): Float {
        return scaleY
    }

    fun setScaleY(scaleY: Float): StagePropViewModel {
        this.scaleY = scaleY
        return this
    }

    fun getRotation(): Integer {
        return rotation
    }

    fun setRotation(rotation: Integer): StagePropViewModel {
        this.rotation = rotation
        return this
    }

    fun getDisplayOrder(): Integer {
        return displayOrder
    }

    fun setDisplayOrder(displayOrder: Integer): StagePropViewModel {
        this.displayOrder = displayOrder
        return this
    }

    @Override
    fun equals(o: Object?): Boolean {
        if (this === o) return true
        if (o == null || getClass() !== o!!.getClass()) return false
        val that = o
        return Float.compare(that!!.scaleX, scaleX) === 0 &&
                Float.compare(that!!.scaleY, scaleY) === 0 &&
                Objects.equals(uuid, that!!.uuid) &&
                Objects.equals(stageId, that.stageId) &&
                Objects.equals(code, that.code) &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type) &&
                Objects.equals(ledCount, that.ledCount) &&
                Objects.equals(positionX, that.positionX) &&
                Objects.equals(positionY, that.positionY) &&
                Objects.equals(rotation, that.rotation) &&
                Objects.equals(displayOrder, that.displayOrder)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(uuid, stageId, code, name, type, ledCount, positionX, positionY, scaleX, scaleY, rotation, displayOrder)
    }

    @Override
    fun toString(): String {
        return "StagePropViewModel{" +
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
