package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "stage")
public class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "name", nullable = false, length = 64)
    private String name;

    @Basic
    @Column(name = "width", nullable = false)
    private int width;

    @Basic
    @Column(name = "height", nullable = false)
    private int height;

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "stageId", targetEntity = StageProp.class)
    private List<StageProp> stageProps = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Stage setName(String name) {
        this.name = name;
        return this;
    }

    public int getWidth() {
        return width;
    }

    public Stage setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public Stage setHeight(int height) {
        this.height = height;
        return this;
    }

    public List<StageProp> getStageProps() {
        return stageProps;
    }

    public void setStageProps(List<StageProp> stageProps) {
        this.stageProps = stageProps;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stage stage = (Stage) o;
        return width == stage.width &&
                height == stage.height &&
                Objects.equals(id, stage.id) &&
                Objects.equals(name, stage.name) &&
                Objects.equals(stageProps, stage.stageProps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, width, height, stageProps);
    }
}
