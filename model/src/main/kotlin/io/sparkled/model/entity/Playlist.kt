package io.sparkled.model.entity

import javax.persistence.*
import java.util.Objects

@Entity
@Table(name = "playlist")
class Playlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private var id: Integer? = null

    @Basic
    @Column(name = "name", nullable = false, length = 64)
    private var name: String? = null

    fun getId(): Integer {
        return id
    }

    fun setId(id: Integer): Playlist {
        this.id = id
        return this
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String): Playlist {
        this.name = name
        return this
    }

    @Override
    fun equals(o: Object?): Boolean {
        if (this === o) return true
        if (o == null || getClass() !== o!!.getClass()) return false
        val playlist = o
        return Objects.equals(id, playlist!!.id) && Objects.equals(name, playlist.name)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(id, name)
    }

    @Override
    fun toString(): String {
        return "Playlist{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}'
    }
}
