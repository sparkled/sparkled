package io.sparkled.viewmodel.stage.prop

import io.sparkled.viewmodel.ViewModel
import java.util.UUID

class StagePropViewModel : ViewModel {

    private var uuid: UUID? = null
    private var stageId: Int? = null
    private var code: String? = null
    private var name: String? = null
    private var type: String? = null
    private var ledCount: Int? = null
    private var reverse: Boolean? = null
    private var positionX: Int? = null
    private var positionY: Int? = null
    private var scaleX: Float? = null
    private var scaleY: Float? = null
    private var rotation: Int? = null
    private var brightness: Int? = null
    private var displayOrder: Int? = null

    fun getUuid(): UUID? {
        return uuid
    }

    fun setUuid(uuid: UUID?): StagePropViewModel {
        this.uuid = uuid
        return this
    }

    fun getStageId(): Int? {
        return stageId
    }

    fun setStageId(stageId: Int?): StagePropViewModel {
        this.stageId = stageId
        return this
    }

    fun getCode(): String? {
        return code
    }

    fun setCode(code: String?): StagePropViewModel {
        this.code = code
        return this
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?): StagePropViewModel {
        this.name = name
        return this
    }

    fun getType(): String? {
        return type
    }

    fun setType(type: String?): StagePropViewModel {
        this.type = type
        return this
    }

    fun getLedCount(): Int? {
        return ledCount
    }

    fun setLedCount(ledCount: Int?): StagePropViewModel {
        this.ledCount = ledCount
        return this
    }

    fun isReverse(): Boolean? {
        return reverse
    }

    fun setReverse(reverse: Boolean?): StagePropViewModel {
        this.reverse = reverse
        return this
    }

    fun getPositionX(): Int? {
        return positionX
    }

    fun setPositionX(positionX: Int?): StagePropViewModel {
        this.positionX = positionX
        return this
    }

    fun getPositionY(): Int? {
        return positionY
    }

    fun setPositionY(positionY: Int?): StagePropViewModel {
        this.positionY = positionY
        return this
    }

    fun getScaleX(): Float? {
        return scaleX
    }

    fun setScaleX(scaleX: Float?): StagePropViewModel {
        this.scaleX = scaleX
        return this
    }

    fun getScaleY(): Float? {
        return scaleY
    }

    fun setScaleY(scaleY: Float?): StagePropViewModel {
        this.scaleY = scaleY
        return this
    }

    fun getRotation(): Int? {
        return rotation
    }

    fun setRotation(rotation: Int?): StagePropViewModel {
        this.rotation = rotation
        return this
    }

    fun getBrightness(): Int? {
        return brightness
    }

    fun setBrightness(brightness: Int?): StagePropViewModel {
        this.brightness = brightness
        return this
    }

    fun getDisplayOrder(): Int? {
        return displayOrder
    }

    fun setDisplayOrder(displayOrder: Int?): StagePropViewModel {
        this.displayOrder = displayOrder
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StagePropViewModel

        if (uuid != other.uuid) return false
        if (stageId != other.stageId) return false
        if (code != other.code) return false
        if (name != other.name) return false
        if (type != other.type) return false
        if (ledCount != other.ledCount) return false
        if (reverse != other.reverse) return false
        if (positionX != other.positionX) return false
        if (positionY != other.positionY) return false
        if (scaleX != other.scaleX) return false
        if (scaleY != other.scaleY) return false
        if (rotation != other.rotation) return false
        if (brightness != other.brightness) return false
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
        result = 31 * result + (reverse?.hashCode() ?: 0)
        result = 31 * result + (positionX ?: 0)
        result = 31 * result + (positionY ?: 0)
        result = 31 * result + (scaleX?.hashCode() ?: 0)
        result = 31 * result + (scaleY?.hashCode() ?: 0)
        result = 31 * result + (rotation ?: 0)
        result = 31 * result + (brightness ?: 0)
        result = 31 * result + (displayOrder ?: 0)
        return result
    }

    override fun toString(): String {
        return "StagePropViewModel(uuid=$uuid, stageId=$stageId, code=$code, name=$name, type=$type, ledCount=$ledCount, reverse=$reverse, positionX=$positionX, positionY=$positionY, scaleX=$scaleX, scaleY=$scaleY, rotation=$rotation, brightness=$brightness, displayOrder=$displayOrder)"
    }
}
