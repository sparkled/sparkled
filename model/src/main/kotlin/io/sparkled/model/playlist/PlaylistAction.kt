package io.sparkled.model.playlist

class PlaylistAction {

    private var type: PlaylistActionType? = null
    private var playlistId: Int? = null

    fun getType(): PlaylistActionType? {
        return type
    }

    fun setType(type: PlaylistActionType): PlaylistAction {
        this.type = type
        return this
    }

    fun getPlaylistId(): Int? {
        return playlistId
    }

    fun setPlaylistId(playlistId: Int): PlaylistAction {
        this.playlistId = playlistId
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlaylistAction

        if (type != other.type) return false
        if (playlistId != other.playlistId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type?.hashCode() ?: 0
        result = 31 * result + (playlistId?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "PlaylistAction(type=$type, playlistId=$playlistId)"
    }
}
