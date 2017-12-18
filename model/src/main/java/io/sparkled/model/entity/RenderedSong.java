package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "rendered_song")
public class RenderedSong {

    private int songId;
    private String renderData;

    @Id
    @Column(name = "song_id", nullable = false)
    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    @Basic
    @Column(name = "render_data", columnDefinition = "text")
    public String getRenderData() {
        return renderData;
    }

    public void setRenderData(String renderData) {
        this.renderData = renderData;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenderedSong songData = (RenderedSong) o;
        return songId == songData.songId &&
                Objects.equals(renderData, songData.renderData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songId, renderData);
    }
}
