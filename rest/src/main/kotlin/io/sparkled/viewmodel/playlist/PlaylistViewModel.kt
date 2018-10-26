package io.sparkled.viewmodel.playlist

import io.sparkled.viewmodel.ViewModel
import io.sparkled.viewmodel.playlist.sequence.PlaylistSequenceViewModel
import java.util.*

class PlaylistViewModel : ViewModel {

    private var id: Int? = null
    private var name: String? = null
    private var sequences: List<PlaylistSequenceViewModel> = ArrayList()

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?): PlaylistViewModel {
        this.id = id
        return this
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?): PlaylistViewModel {
        this.name = name
        return this
    }

    fun getSequences(): List<PlaylistSequenceViewModel> {
        return sequences
    }

    fun setSequences(sequences: List<PlaylistSequenceViewModel>): PlaylistViewModel {
        this.sequences = sequences
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PlaylistViewModel

        if (id != other.id) return false
        if (name != other.name) return false
        if (sequences != other.sequences) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + sequences.hashCode()
        return result
    }

    override fun toString(): String {
        return "PlaylistViewModel(id=$id, name=$name, sequences=$sequences)"
    }
}
