package io.sparkled.viewmodel.song;

import io.sparkled.viewmodel.ViewModel;

import java.util.Objects;

public class SongViewModel implements ViewModel {

    private Integer id;
    private String name;
    private String artist;
    private String album;
    private Integer durationMs;

    public Integer getId() {
        return id;
    }

    public SongViewModel setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public SongViewModel setName(String name) {
        this.name = name;
        return this;
    }

    public String getArtist() {
        return artist;
    }

    public SongViewModel setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public String getAlbum() {
        return album;
    }

    public SongViewModel setAlbum(String album) {
        this.album = album;
        return this;
    }

    public Integer getDurationMs() {
        return durationMs;
    }

    public SongViewModel setDurationMs(Integer durationMs) {
        this.durationMs = durationMs;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SongViewModel that = (SongViewModel) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(artist, that.artist) &&
                Objects.equals(album, that.album) &&
                Objects.equals(durationMs, that.durationMs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, artist, album, durationMs);
    }

    @Override
    public String toString() {
        return "SongViewModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", durationMs=" + durationMs +
                '}';
    }
}
