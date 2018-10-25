package io.sparkled.viewmodel.playlist

import io.sparkled.viewmodel.ViewModel
import io.sparkled.viewmodel.playlist.sequence.PlaylistSequenceViewModel

import java.util.ArrayList
import java.util.Objects

class PlaylistViewModel : ViewModel {

    private var id: Integer? = null
    private var name: String? = null
    private var sequences: List<PlaylistSequenceViewModel> = ArrayList()

    fun getId(): Integer {
        return id
    }

    fun setId(id: Integer): PlaylistViewModel {
        this.id = id
        return this
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String): PlaylistViewModel {
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

    @Override
    fun equals(o: Object?): Boolean {
        if (this === o) return true
        if (o == null || getClass() !== o!!.getClass()) return false
        val that = o
        return Objects.equals(id, that!!.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(sequences, that.sequences)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(id, name, sequences)
    }

    @Override
    fun toString(): String {
        return "PlaylistViewModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sequences=" + sequences +
                '}'
    }
}
