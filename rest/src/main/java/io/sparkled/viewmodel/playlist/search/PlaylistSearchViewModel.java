package io.sparkled.viewmodel.playlist.search;

import io.sparkled.viewmodel.ViewModel;
import io.sparkled.viewmodel.playlist.sequence.PlaylistSequenceViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlaylistSearchViewModel implements ViewModel {

    private Integer id;
    private String name;

    public Integer getId() {
        return id;
    }

    public PlaylistSearchViewModel setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PlaylistSearchViewModel setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistSearchViewModel that = (PlaylistSearchViewModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public String toString() {
        return "PlaylistSearchViewModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
