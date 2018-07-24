package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "stage_channel")
public class StageChannel {

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
    @Column(name = "position_x", nullable = false)
    private int positionX = 0;

    @Basic
    @Column(name = "position_y", nullable = false)
    private int positionY = 0;

    @Basic
    @Column(name = "scale_x", nullable = false)
    private int scaleX = 1;

    @Basic
    @Column(name = "scale_y", nullable = false)
    private int scaleY = 1;

    @Basic
    @Column(name = "rotation", nullable = false)
    private int rotation = 0;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getStageId() {
        return stageId;
    }

    public StageChannel setStageId(Integer stageId) {
        this.stageId = stageId;
        return this;
    }

    public String getCode() {
        return code;
    }

    public StageChannel setCode(String code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public StageChannel setName(String name) {
        this.name = name;
        return this;
    }

    public String getType() {
        return type;
    }

    public StageChannel setType(String type) {
        this.type = type;
        return this;
    }

    public int getPositionX() {
        return positionX;
    }

    public void setPositionX(int positionX) {
        this.positionX = positionX;
    }

    public int getPositionY() {
        return positionY;
    }

    public void setPositionY(int positionY) {
        this.positionY = positionY;
    }

    public int getScaleX() {
        return scaleX;
    }

    public void setScaleX(int scaleX) {
        this.scaleX = scaleX;
    }

    public int getScaleY() {
        return scaleY;
    }

    public void setScaleY(int scaleY) {
        this.scaleY = scaleY;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(int rotation) {
        this.rotation = rotation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StageChannel that = (StageChannel) o;
        return positionX == that.positionX &&
                positionY == that.positionY &&
                scaleX == that.scaleX &&
                scaleY == that.scaleY &&
                rotation == that.rotation &&
                Objects.equals(id, that.id) &&
                Objects.equals(stageId, that.stageId) &&
                Objects.equals(code, that.code) &&
                Objects.equals(name, that.name) &&
                Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, stageId, code, name, type, positionX, positionY, scaleX, scaleY, rotation);
    }
}
