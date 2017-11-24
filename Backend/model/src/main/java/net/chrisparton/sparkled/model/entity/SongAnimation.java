package net.chrisparton.sparkled.model.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "song_animation")
public class SongAnimation {

    private int songId;
    private String animationData;

    @Id
    @Column(name = "song_id", nullable = false)
    public int getSongId() {
        return songId;
    }

    public SongAnimation setSongId(int songId) {
        this.songId = songId;
        return this;
    }

    @Basic
    @Column(name = "animation_data", columnDefinition = "text")
    public String getAnimationData() {
        return animationData;
    }

    public SongAnimation setAnimationData(String animationData) {
        this.animationData = animationData;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SongAnimation songData = (SongAnimation) o;
        return songId == songData.songId &&
                Objects.equals(animationData, songData.animationData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songId, animationData);
    }
}
