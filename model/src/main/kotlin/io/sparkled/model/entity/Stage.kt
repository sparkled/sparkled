package io.sparkled.model.entity

import javax.persistence.*
import java.util.Objects

@Entity
@Table(name = "stage")
class Stage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private var id: Integer? = null

    @Basic
    @Column(name = "name")
    private var name: String? = null

    @Basic
    @Column(name = "width")
    private var width: Integer? = null

    @Basic
    @Column(name = "height")
    private var height: Integer? = null

    fun getId(): Integer {
        return id
    }

    fun setId(id: Integer): Stage {
        this.id = id
        return this
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String): Stage {
        this.name = name
        return this
    }

    fun getWidth(): Integer {
        return width
    }

    fun setWidth(width: Integer): Stage {
        this.width = width
        return this
    }

    fun getHeight(): Integer {
        return height
    }

    fun setHeight(height: Integer): Stage {
        this.height = height
        return this
    }

    @Override
    fun equals(o: Object?): Boolean {
        if (this === o) return true
        if (o == null || getClass() !== o!!.getClass()) return false
        val stage = o
        return Objects.equals(id, stage!!.id) &&
                Objects.equals(name, stage.name) &&
                Objects.equals(width, stage.width) &&
                Objects.equals(height, stage.height)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(id, name, width, height)
    }

    @Override
    fun toString(): String {
        return "Stage{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", width=" + width +
                ", height=" + height +
                '}'
    }
}
