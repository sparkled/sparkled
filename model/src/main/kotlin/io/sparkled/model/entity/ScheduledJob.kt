package io.sparkled.model.entity

import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "scheduled_job")
class ScheduledJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    var id: Int? = null

    @Enumerated(EnumType.STRING)
    @Column(name = "action")
    var action: ScheduledJobAction? = null

    @Basic
    @Column(name = "cron_expression")
    var cronExpression: String? = null

    @Basic
    @Column(name = "value")
    var value: String? = null

    @Basic
    @Column(name = "playlist_id")
    var playlistId: Int? = null

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ScheduledJob

        if (id != other.id) return false
        if (action != other.action) return false
        if (cronExpression != other.cronExpression) return false
        if (value != other.value) return false
        if (playlistId != other.playlistId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (action?.hashCode() ?: 0)
        result = 31 * result + (cronExpression?.hashCode() ?: 0)
        result = 31 * result + (value?.hashCode() ?: 0)
        result = 31 * result + (playlistId ?: 0)
        return result
    }

    override fun toString(): String {
        return "ScheduledJob(id=$id, action=$action, cronExpression=$cronExpression, value=$value, playlistId=$playlistId)"
    }
}
