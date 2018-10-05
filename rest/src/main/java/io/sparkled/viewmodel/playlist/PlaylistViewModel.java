package io.sparkled.viewmodel.playlist;

import io.sparkled.viewmodel.playlist.sequence.PlaylistSequenceViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PlaylistViewModel {

    private Integer id;
    private String name;
    private List<PlaylistSequenceViewModel> sequences = new ArrayList<>();

    public Integer getId() {
        return id;
    }

    public PlaylistViewModel setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PlaylistViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public List<PlaylistSequenceViewModel> getSequences() {
        return sequences;
    }

    public PlaylistViewModel setSequences(List<PlaylistSequenceViewModel> sequences) {
        this.sequences = sequences;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistViewModel that = (PlaylistViewModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(sequences, that.sequences);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, sequences);
    }

    @Override
    public String toString() {
        return "PlaylistViewModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sequences=" + sequences +
                '}';
    }
}
