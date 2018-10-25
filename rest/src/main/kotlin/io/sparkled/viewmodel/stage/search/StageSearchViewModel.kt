package io.sparkled.viewmodel.stage.search

import io.sparkled.viewmodel.ViewModel

class StageSearchViewModel : ViewModel {
    private var id: Int? = null
    private var name: String? = null

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int): StageSearchViewModel {
        this.id = id
        return this
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String): StageSearchViewModel {
        this.name = name
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StageSearchViewModel

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
        return "StageSearchViewModel(id=$id, name=$name)"
    }
}
