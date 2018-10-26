package io.sparkled.model.entity

import javax.persistence.*

@Entity
@Table(name = "playlist")
class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private var id: Int? = null

    @Basic
    @Column(name = "name", nullable = false, length = 64)
    private var name: String? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int): Playlist {
        this.id = id
        return this
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?): Playlist {
        this.name = name
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Playlist

        if (id != other.id) return false
        if (name != other.name) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Playlist(id=$id, name=$name)"
    }
}
