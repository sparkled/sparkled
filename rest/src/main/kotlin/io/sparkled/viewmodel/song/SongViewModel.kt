package io.sparkled.viewmodel.song

import io.sparkled.viewmodel.ViewModel

class SongViewModel : ViewModel {

    private var id: Int? = null
    private var name: String? = null
    private var artist: String? = null
    private var album: String? = null
    private var durationMs: Int? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?): SongViewModel {
        this.id = id
        return this
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?): SongViewModel {
        this.name = name
        return this
    }

    fun getArtist(): String? {
        return artist
    }

    fun setArtist(artist: String?): SongViewModel {
        this.artist = artist
        return this
    }

    fun getAlbum(): String? {
        return album
    }

    fun setAlbum(album: String?): SongViewModel {
        this.album = album
        return this
    }

    fun getDurationMs(): Int? {
        return durationMs
    }

    fun setDurationMs(durationMs: Int?): SongViewModel {
        this.durationMs = durationMs
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SongViewModel

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
        return "SongViewModel(id=$id, name=$name, artist=$artist, album=$album, durationMs=$durationMs)"
    }
}
