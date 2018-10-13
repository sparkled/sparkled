package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "song")
public class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Basic
    @Column(name = "name")
    private String name;

    @Basic
    @Column(name = "artist")
    private String artist;

    @Basic
    @Column(name = "album")
    private String album;

    @Basic
    @Column(name = "duration_ms")
    private Integer durationMs;

    public Integer getId() {
        return id;
    }

    public Song setId(Integer id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Song setName(String name) {
        this.name = name;
        return this;
    }

    public String getArtist() {
        return artist;
    }

    public Song setArtist(String artist) {
        this.artist = artist;
        return this;
    }

    public String getAlbum() {
        return album;
    }

    public Song setAlbum(String album) {
        this.album = album;
        return this;
    }

    public Integer getDurationMs() {
        return durationMs;
    }

    public Song setDurationMs(Integer durationMs) {
        this.durationMs = durationMs;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return Objects.equals(id, song.id) &&
                Objects.equals(name, song.name) &&
                Objects.equals(artist, song.artist) &&
                Objects.equals(album, song.album) &&
                Objects.equals(durationMs, song.durationMs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, artist, album, durationMs);
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", durationMs=" + durationMs +
                '}';
    }
}
