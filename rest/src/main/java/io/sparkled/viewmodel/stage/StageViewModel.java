package io.sparkled.viewmodel.stage;

import io.sparkled.viewmodel.ViewModel;
import io.sparkled.viewmodel.stage.prop.StagePropViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class StageViewModel implements ViewModel {
    private Integer id;
    private String name;
    private Integer width;
    private Integer height;
    private List<StagePropViewModel> stageProps = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public StageViewModel setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public StageViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    public StageViewModel setWidth(Integer width) {
        this.width = width;
        return this;
    }

    public Integer getHeight() {
        return height;
    }

    public StageViewModel setHeight(Integer height) {
        this.height = height;
        return this;
    }

    public List<StagePropViewModel> getStageProps() {
        return stageProps;
    }

    public StageViewModel setStageProps(List<StagePropViewModel> stageProps) {
        this.stageProps = stageProps;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StageViewModel that = (StageViewModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(width, that.width) &&
                Objects.equals(height, that.height) &&
                Objects.equals(stageProps, that.stageProps);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, width, height, stageProps);
    }

    @Override
    public String toString() {
        return "StageViewModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", stageProps=" + stageProps +
                '}';
    }
}
