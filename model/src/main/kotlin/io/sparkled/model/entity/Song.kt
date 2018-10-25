package io.sparkled.model.entity

import javax.persistence.*

@Entity
@Table(name = "song")
class Song {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private var id: Int? = null

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
    private var durationMs: Int? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?): Song {
        this.id = id
        return this
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?): Song {
        this.name = name
        return this
    }

    fun getArtist(): String? {
        return artist
    }

    fun setArtist(artist: String?): Song {
        this.artist = artist
        return this
    }

    fun getAlbum(): String? {
        return album
    }

    fun setAlbum(album: String?): Song {
        this.album = album
        return this
    }

    fun getDurationMs(): Int? {
        return durationMs
    }

    fun setDurationMs(durationMs: Int?): Song {
        this.durationMs = durationMs
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Song

        if (id != other.id) return false
        if (name != other.name) return false
        if (artist != other.artist) return false
        if (album != other.album) return false
        if (durationMs != other.durationMs) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (artist?.hashCode() ?: 0)
        result = 31 * result + (album?.hashCode() ?: 0)
        result = 31 * result + (durationMs ?: 0)
        return result
    }

    override fun toString(): String {
        return "Song(id=$id, name=$name, artist=$artist, album=$album, durationMs=$durationMs)"
    }
}
