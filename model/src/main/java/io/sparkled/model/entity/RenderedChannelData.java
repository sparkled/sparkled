package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "rendered_channel_data")
public class RenderedChannelData {

    private int id;
    private int songId;
    private String channelCode;
    private int ledCount;
    private byte[] data;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public RenderedChannelData setId(int id) {
        this.id = id;
        return this;
    }

    @Column(name = "song_id", nullable = false)
    public int getSongId() {
        return songId;
    }

    public RenderedChannelData setSongId(int songId) {
        this.songId = songId;
        return this;
    }

    @Column(name = "channel_code", nullable = false)
    public String getChannelCode() {
        return channelCode;
    }

    public RenderedChannelData setChannelCode(String channelCode) {
        this.channelCode = channelCode;
        return this;
    }

    @Column(name = "led_count", nullable = false)
    public int getLedCount() {
        return ledCount;
    }

    public RenderedChannelData setLedCount(int ledCount) {
        this.ledCount = ledCount;
        return this;
    }

    @Lob
    @Column(name = "data")
    public byte[] getData() {
        return data;
    }

    public RenderedChannelData setData(byte[] data) {
        this.data = data;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RenderedChannelData that = (RenderedChannelData) o;
        return songId == that.songId &&
                Objects.equals(channelCode, that.channelCode) &&
                Arrays.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(songId, channelCode, data);
    }
}
