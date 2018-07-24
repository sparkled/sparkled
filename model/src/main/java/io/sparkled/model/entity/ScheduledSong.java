package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "scheduled_song")
public class ScheduledSong {

    public static final int MIN_SECONDS_BETWEEN_SONGS = 5;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    private Song song;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time", nullable = false)
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time", nullable = false)
    private Date endTime;

    public ScheduledSong() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ScheduledSong scheduledSong = (ScheduledSong) o;
        return Objects.equals(id, scheduledSong.id) &&
                Objects.equals(song, scheduledSong.song) &&
                Objects.equals(startTime, scheduledSong.startTime) &&
                Objects.equals(endTime, scheduledSong.endTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, song, startTime, endTime);
    }
}
