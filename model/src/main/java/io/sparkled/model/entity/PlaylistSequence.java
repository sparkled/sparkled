package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "playlist_sequence")
public class PlaylistSequence {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Basic
    @Column(name = "playlist_id", nullable = false)
    private Integer playlistId;

    @Basic
    @Column(name = "sequence_id", nullable = false)
    private Integer sequenceId;

    @Basic
    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    public Integer getId() {
        return id;
    }

    public PlaylistSequence setId(Integer id) {
        this.id = id;
        return this;
    }

    public Integer getPlaylistId() {
        return playlistId;
    }

    public PlaylistSequence setPlaylistId(Integer playlistId) {
        this.playlistId = playlistId;
        return this;
    }

    public Integer getSequenceId() {
        return sequenceId;
    }

    public PlaylistSequence setSequenceId(Integer sequenceId) {
        this.sequenceId = sequenceId;
        return this;
    }

    public Integer getDisplayOrder() {
        return displayOrder;
    }

    public PlaylistSequence setDisplayOrder(Integer displayOrder) {
        this.displayOrder = displayOrder;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlaylistSequence that = (PlaylistSequence) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(playlistId, that.playlistId) &&
                Objects.equals(sequenceId, that.sequenceId) &&
                Objects.equals(displayOrder, that.displayOrder);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, playlistId, sequenceId, displayOrder);
    }

    @Override
    public String toString() {
        return "PlaylistSequence{" +
                "id=" + id +
                ", playlistId=" + playlistId +
                ", sequenceId=" + sequenceId +
                ", displayOrder=" + displayOrder +
                '}';
    }
}
