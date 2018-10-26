package io.sparkled.model.entity

import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "stage")
class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private var id: Int? = null

    @Basic
    @Column(name = "name")
    private var name: String? = null

    @Basic
    @Column(name = "width")
    private var width: Int? = null

    @Basic
    @Column(name = "height")
    private var height: Int? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?): Stage {
        this.id = id
        return this
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?): Stage {
        this.name = name
        return this
    }

    fun getWidth(): Int? {
        return width
    }

    fun setWidth(width: Int?): Stage {
        this.width = width
        return this
    }

    fun getHeight(): Int? {
        return height
    }

    fun setHeight(height: Int?): Stage {
        this.height = height
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Stage

        if (id != other.id) return false
        if (name != other.name) return false
        if (width != other.width) return false
        if (height != other.height) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (width?.hashCode() ?: 0)
        result = 31 * result + (height?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Stage(id=$id, name=$name, width=$width, height=$height)"
    }
}
