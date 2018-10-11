package io.sparkled.viewmodel.stage.prop;

import io.sparkled.viewmodel.ViewModel;

import java.util.Objects;
import java.util.UUID;

public class StagePropViewModel implements ViewModel {

    private UUID uuid;
    private Integer stageId;
    private String code;
    private String name;
    private String type;
    private Integer ledCount = 0;
    private Integer positionX = 0;
    private Integer positionY = 0;
    private float scaleX = 1;
    private float scaleY = 1;
    private Integer rotation = 0;
    private Integer displayOrder = 0;

    public UUID getUuid() {
        return uuid;
    }

    public StagePropViewModel setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public Integer getStageId() {
        return stageId;
    }

    public StagePropViewModel setStageId(Integer stageId) {
        this.stageId = stageId;
        return this;
    }

    public String getCode() {
        return code;
    }

    public StagePropViewModel setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public StagePropViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public StagePropViewModel setType(String type) {
        this.type = type;
        return this;
    }

    public Integer getLedCount() {
        return ledCount;
    }

    public StagePropViewModel setLedCount(Integer ledCount) {
        this.ledCount = ledCount;
        return this;
    }

    public Integer getPositionX() {
        return positionX;
    }

    public StagePropViewModel setPositionX(Integer positionX) {
        this.positionX = positionX;
        return this;
    }

    public Integer getPositionY() {
        return positionY;
    }

    public StagePropViewModel setPositionY(Integer positionY) {
        this.positionY = positionY;
        return this;
    }

    public float getScaleX() {
        return scaleX;
    }

    public StagePropViewModel setScaleX(float scaleX) {
        this.scaleX = scaleX;
        return this;
    }

    public float getScaleY() {
        return scaleY;
    }

    public StagePropViewModel setScaleY(float scaleY) {
        this.scaleY = scaleY;
        return this;
    }

    public Integer getRotation() {
        return rotation;
    }

    public StagePropViewModel setRotation(Integer rotation) {
        this.rotation = rotation;
        return this;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public StagePropViewModel setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StagePropViewModel that = (StagePropViewModel) o;
        return Float.compare(that.scaleX, scaleX) == 0 &&
                Float.compare(that.scaleY, scaleY) == 0 &&
                Objects.equals(uuid, that.uuid) &&
                Objects.equals(stageId, that.stageId) &&
                Objects.equals(code, that.code) &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type) &&
                Objects.equals(ledCount, that.ledCount) &&
                Objects.equals(positionX, that.positionX) &&
                Objects.equals(positionY, that.positionY) &&
                Objects.equals(rotation, that.rotation) &&
                Objects.equals(displayOrder, that.displayOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, stageId, code, name, type, ledCount, positionX, positionY, scaleX, scaleY, rotation, displayOrder);
    }

    @Override
    public String toString() {
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
                '}';
    }
}
