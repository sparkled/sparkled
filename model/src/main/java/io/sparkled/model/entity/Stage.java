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
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "width")
    private Integer width;

    @Basic
    @Column(name = "height")
    private Integer height;

    public Integer getId() {
        return id;
    }

    public Stage setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Stage setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    public Stage setWidth(Integer width) {
        this.width = width;
        return this;
    }

    public Integer getHeight() {
        return height;
    }

    public Stage setHeight(Integer height) {
        this.height = height;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stage stage = (Stage) o;
        return Objects.equals(id, stage.id) &&
                Objects.equals(name, stage.name) &&
                Objects.equals(width, stage.width) &&
                Objects.equals(height, stage.height);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, width, height);
    }

    @Override
    public String toString() {
        return "Stage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
