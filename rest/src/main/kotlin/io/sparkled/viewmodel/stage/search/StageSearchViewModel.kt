package io.sparkled.viewmodel.stage.search

import io.sparkled.viewmodel.ViewModel

import java.util.Objects

class StageSearchViewModel : ViewModel {
    private var id: Integer? = null
    private var name: String? = null

    fun getId(): Integer {
        return id
    }

    fun setId(id: Integer): StageSearchViewModel {
        this.id = id
        return this
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String): StageSearchViewModel {
        this.name = name
        return this
    }


    @Override
    fun equals(o: Object?): Boolean {
        if (this === o) return true
        if (o == null || getClass() !== o!!.getClass()) return false
        val that = o
        return Objects.equals(id, that!!.id) && Objects.equals(name, that.name)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(id, name)
    }

    @Override
    fun toString(): String {
        return "StageSearchViewModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}'
    }
}
