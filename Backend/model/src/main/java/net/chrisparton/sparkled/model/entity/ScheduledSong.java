package net.chrisparton.sparkled.model.entity;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "scheduled_song")
public class ScheduledSong {

    private Integer id;
    private Song song;
    private Date startTime;
    private Date endTime;

    public ScheduledSong() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @ManyToOne
    public Song getSong() {
        return song;
    }

    public void setSong(Song song) {
        this.song = song;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time", nullable = false)
    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time", nullable = false)
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
