package io.sparkled.viewmodel.song

import io.sparkled.viewmodel.ViewModel

import java.util.Objects

class SongViewModel : ViewModel {

    private var id: Integer? = null
    private var name: String? = null
    private var artist: String? = null
    private var album: String? = null
    private var durationMs: Integer? = null

    fun getId(): Integer {
        return id
    }

    fun setId(id: Integer): SongViewModel {
        this.id = id
        return this
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String): SongViewModel {
        this.name = name
        return this
    }

    fun getArtist(): String {
        return artist
    }

    fun setArtist(artist: String): SongViewModel {
        this.artist = artist
        return this
    }

    fun getAlbum(): String {
        return album
    }

    fun setAlbum(album: String): SongViewModel {
        this.album = album
        return this
    }

    fun getDurationMs(): Integer {
        return durationMs
    }

    fun setDurationMs(durationMs: Integer): SongViewModel {
        this.durationMs = durationMs
        return this
    }

    @Override
    fun equals(o: Object?): Boolean {
        if (this === o) return true
        if (o == null || getClass() !== o!!.getClass()) return false
        val that = o
        return Objects.equals(id, that!!.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(artist, that.artist) &&
                Objects.equals(album, that.album) &&
                Objects.equals(durationMs, that.durationMs)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(id, name, artist, album, durationMs)
    }

    @Override
    fun toString(): String {
        return "SongViewModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", durationMs=" + durationMs +
                '}'
    }
}
