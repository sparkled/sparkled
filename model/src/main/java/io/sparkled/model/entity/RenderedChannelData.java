package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Table(name = "rendered_channel_data")
public class RenderedChannelData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "song_id", nullable = false)
    private int songId;

    @Column(name = "channel_code", nullable = false)
    private String channelCode;

    @Column(name = "led_count", nullable = false)
    private int ledCount;

    @Lob
    @Column(name = "data")
    private byte[] data;


    public int getId() {
        return id;
    }

    public RenderedChannelData setId(int id) {
        this.id = id;
        return this;
    }

    public int getSongId() {
        return songId;
    }

    public RenderedChannelData setSongId(int songId) {
        this.songId = songId;
        return this;
    }

    public String getChannelCode() {
        return channelCode;
    }

    public RenderedChannelData setChannelCode(String channelCode) {
        this.channelCode = channelCode;
        return this;
    }

    public int getLedCount() {
        return ledCount;
    }

    public RenderedChannelData setLedCount(int ledCount) {
        this.ledCount = ledCount;
        return this;
    }

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
