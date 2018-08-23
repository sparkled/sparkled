package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "stage_prop")
public class StageProp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "stage_id", nullable = false)
    private Integer stageId;

    @Basic
    @Column(name = "code", nullable = false, length = 8)
    private String code;

    @Basic
    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @Basic
    @Column(name = "type", nullable = false, length = 32)
    private String type;

    @Basic
    @Column(name = "leds", nullable = false)
    private int leds = 0;

    @Basic
    @Column(name = "position_x", nullable = false)
    private int positionX = 0;

    @Basic
    @Column(name = "position_y", nullable = false)
    private int positionY = 0;

    @Basic
    @Column(name = "scale_x", nullable = false)
    private float scaleX = 1;

    @Basic
    @Column(name = "scale_y", nullable = false)
    private float scaleY = 1;

    @Basic
    @Column(name = "rotation", nullable = false)
    private int rotation = 0;

    @Basic
    @Column(name = "display_order", nullable = false)
    private int displayOrder = 0;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public int getLeds() {
        return leds;
    }

    public StageProp setLeds(int leds) {
        this.leds = leds;
        return this;
    }

    public int getPositionX() {
        return positionX;
    }

    public StageProp setPositionX(int positionX) {
        this.positionX = positionX;
        return this;
    }

    public int getPositionY() {
        return positionY;
    }

    public StageProp setPositionY(int positionY) {
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

    public int getRotation() {
        return rotation;
    }

    public StageProp setRotation(int rotation) {
        this.rotation = rotation;
        return this;
    }

    public int getDisplayOrder() {
        return displayOrder;
    }

    public StageProp setDisplayOrder(int displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StageProp that = (StageProp) o;
        return leds == that.leds &&
                positionX == that.positionX &&
                positionY == that.positionY &&
                scaleX == that.scaleX &&
                scaleY == that.scaleY &&
                rotation == that.rotation &&
                displayOrder == that.displayOrder &&
                Objects.equals(id, that.id) &&
                Objects.equals(stageId, that.stageId) &&
                Objects.equals(code, that.code) &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stageId, code, name, type, leds, positionX, positionY, scaleX, scaleY, rotation, displayOrder);
    }
}
