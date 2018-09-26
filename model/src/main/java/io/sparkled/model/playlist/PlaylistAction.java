package io.sparkled.model.playlist;

import java.util.Objects;

public class PlaylistAction {

    private PlaylistActionType type;
    private Integer playlistId;

    public PlaylistActionType getType() {
        return type;
    }

    public PlaylistAction setType(PlaylistActionType type) {
        this.type = type;
        return this;
    }

    public Integer getPlaylistId() {
        return playlistId;
    }

    public PlaylistAction setPlaylistId(Integer playlistId) {
        this.playlistId = playlistId;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistAction that = (PlaylistAction) o;
        return type == that.type &&
                Objects.equals(playlistId, that.playlistId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, playlistId);
    }

    @Override
    public String toString() {
        return "PlaylistAction{" +
                "type=" + type +
                ", playlistId=" + playlistId +
                '}';
    }
}
