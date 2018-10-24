package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "stage_prop")
public class StageProp {

    @Id
    @Column(name = "uuid")
    private UUID uuid;

    @Basic
    @Column(name = "stage_id")
    private Integer stageId;

    @Basic
    @Column(name = "code")
    private String code;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "type")
    private String type;

    @Basic
    @Column(name = "led_count")
    private Integer ledCount = 0;

    @Basic
    @Column(name = "position_x")
    private Integer positionX = 0;

    @Basic
    @Column(name = "position_y")
    private Integer positionY = 0;

    @Basic
    @Column(name = "scale_x")
    private float scaleX = 1;

    @Basic
    @Column(name = "scale_y")
    private float scaleY = 1;

    @Basic
    @Column(name = "rotation")
    private Integer rotation = 0;

    @Basic
    @Column(name = "display_order")
    private Integer displayOrder = 0;

    public UUID getUuid() {
        return uuid;
    }

    public StageProp setUuid(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    public Integer getStageId() {
        return stageId;
    }

    public StageProp setStageId(Integer stageId) {
        this.stageId = stageId;
        return this;
    }

    public String getCode() {
        return code;
    }

    public StageProp setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public StageProp setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public StageProp setType(String type) {
        this.type = type;
        return this;
    }

    public Integer getLedCount() {
        return ledCount;
    }

    public StageProp setLedCount(Integer ledCount) {
        this.ledCount = ledCount;
        return this;
    }

    public Integer getPositionX() {
        return positionX;
    }

    public StageProp setPositionX(Integer positionX) {
        this.positionX = positionX;
        return this;
    }

    public Integer getPositionY() {
        return positionY;
    }

    public StageProp setPositionY(Integer positionY) {
        this.positionY = positionY;
        return this;
    }

    public float getScaleX() {
        return scaleX;
    }

    public StageProp setScaleX(float scaleX) {
        this.scaleX = scaleX;
        return this;
    }

    public float getScaleY() {
        return scaleY;
    }

    public StageProp setScaleY(float scaleY) {
        this.scaleY = scaleY;
        return this;
    }

    public Integer getRotation() {
        return rotation;
    }

    public StageProp setRotation(Integer rotation) {
        this.rotation = rotation;
        return this;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public StageProp setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StageProp stageProp = (StageProp) o;
        return Float.compare(stageProp.scaleX, scaleX) == 0 &&
                Float.compare(stageProp.scaleY, scaleY) == 0 &&
                Objects.equals(uuid, stageProp.uuid) &&
                Objects.equals(stageId, stageProp.stageId) &&
                Objects.equals(code, stageProp.code) &&
                Objects.equals(name, stageProp.name) &&
                Objects.equals(type, stageProp.type) &&
                Objects.equals(ledCount, stageProp.ledCount) &&
                Objects.equals(positionX, stageProp.positionX) &&
                Objects.equals(positionY, stageProp.positionY) &&
                Objects.equals(rotation, stageProp.rotation) &&
                Objects.equals(displayOrder, stageProp.displayOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, stageId, code, name, type, ledCount, positionX, positionY, scaleX, scaleY, rotation, displayOrder);
    }

    @Override
    public String toString() {
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
                '}';
    }
}
