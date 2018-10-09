package io.sparkled.viewmodel.sequence.search;

import io.sparkled.model.entity.SequenceStatus;
import io.sparkled.viewmodel.ViewModel;

import java.util.Objects;

public class SequenceSearchViewModel implements ViewModel {
    private Integer id;
    private String name;
    private String artist;
    private String album;
    private Integer durationFrames;
    private Integer framesPerSecond;
    private SequenceStatus status;

    public Integer getId() {
        return id;
    }

    public SequenceSearchViewModel setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public SequenceSearchViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getArtist() {
        return artist;
    }

    public SequenceSearchViewModel setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public String getAlbum() {
        return album;
    }

    public SequenceSearchViewModel setAlbum(String album) {
        this.album = album;
        return this;
    }

    public Integer getDurationFrames() {
        return durationFrames;
    }

    public SequenceSearchViewModel setDurationFrames(Integer durationFrames) {
        this.durationFrames = durationFrames;
        return this;
    }

    public Integer getFramesPerSecond() {
        return framesPerSecond;
    }

    public SequenceSearchViewModel setFramesPerSecond(Integer framesPerSecond) {
        this.framesPerSecond = framesPerSecond;
        return this;
    }

    public SequenceStatus getStatus() {
        return status;
    }

    public SequenceSearchViewModel setStatus(SequenceStatus status) {
        this.status = status;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SequenceSearchViewModel that = (SequenceSearchViewModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(artist, that.artist) &&
                Objects.equals(album, that.album) &&
                Objects.equals(durationFrames, that.durationFrames) &&
                Objects.equals(framesPerSecond, that.framesPerSecond) &&
                status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, artist, album, durationFrames, framesPerSecond, status);
    }

    @Override
    public String toString() {
        return "SequenceSearchViewModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", durationFrames=" + durationFrames +
                ", framesPerSecond=" + framesPerSecond +
                ", status=" + status +
                '}';
    }
}
