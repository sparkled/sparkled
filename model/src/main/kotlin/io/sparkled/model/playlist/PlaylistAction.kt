package io.sparkled.model.playlist

import java.util.Objects

class PlaylistAction {

    private var type: PlaylistActionType? = null
    private var playlistId: Integer? = null

    fun getType(): PlaylistActionType {
        return type
    }

    fun setType(type: PlaylistActionType): PlaylistAction {
        this.type = type
        return this
    }

    fun getPlaylistId(): Integer {
        return playlistId
    }

    fun setPlaylistId(playlistId: Integer): PlaylistAction {
        this.playlistId = playlistId
        return this
    }

    @Override
    fun equals(o: Object?): Boolean {
        if (this === o) return true
        if (o == null || getClass() !== o!!.getClass()) return false
        val that = o
        return type === that!!.type && Objects.equals(playlistId, that!!.playlistId)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(type, playlistId)
    }

    @Override
    fun toString(): String {
        return "PlaylistAction{" +
                "type=" + type +
                ", playlistId=" + playlistId +
                '}'
    }
}
