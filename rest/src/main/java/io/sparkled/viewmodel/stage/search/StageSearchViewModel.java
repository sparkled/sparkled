package io.sparkled.viewmodel.stage.search;

import io.sparkled.viewmodel.ViewModel;

import java.util.Objects;

public class StageSearchViewModel implements ViewModel {
    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public StageSearchViewModel setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public StageSearchViewModel setName(String name) {
        this.name = name;
        return this;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StageSearchViewModel that = (StageSearchViewModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "StageSearchViewModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
