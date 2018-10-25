package io.sparkled.model.entity

import javax.persistence.*
import java.util.Objects

@Entity
@Table(name = "song")
class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private var id: Integer? = null

    @Basic
    @Column(name = "name")
    private var name: String? = null

    @Basic
    @Column(name = "artist")
    private var artist: String? = null

    @Basic
    @Column(name = "album")
    private var album: String? = null

    @Basic
    @Column(name = "duration_ms")
    private var durationMs: Integer? = null

    fun getId(): Integer {
        return id
    }

    fun setId(id: Integer): Song {
        this.id = id
        return this
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String): Song {
        this.name = name
        return this
    }

    fun getArtist(): String {
        return artist
    }

    fun setArtist(artist: String): Song {
        this.artist = artist
        return this
    }

    fun getAlbum(): String {
        return album
    }

    fun setAlbum(album: String): Song {
        this.album = album
        return this
    }

    fun getDurationMs(): Integer {
        return durationMs
    }

    fun setDurationMs(durationMs: Integer): Song {
        this.durationMs = durationMs
        return this
    }

    @Override
    fun equals(o: Object?): Boolean {
        if (this === o) return true
        if (o == null || getClass() !== o!!.getClass()) return false
        val song = o
        return Objects.equals(id, song!!.id) &&
                Objects.equals(name, song.name) &&
                Objects.equals(artist, song.artist) &&
                Objects.equals(album, song.album) &&
                Objects.equals(durationMs, song.durationMs)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(id, name, artist, album, durationMs)
    }

    @Override
    fun toString(): String {
        return "Song{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", durationMs=" + durationMs +
                '}'
    }
}
