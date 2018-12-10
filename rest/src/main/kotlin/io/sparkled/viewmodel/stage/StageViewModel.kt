package io.sparkled.viewmodel.stage

import io.sparkled.viewmodel.ViewModel
import io.sparkled.viewmodel.stage.prop.StagePropViewModel

class StageViewModel : ViewModel {
    private var id: Int? = null
    private var name: String? = null
    private var width: Int? = null
    private var height: Int? = null
    private var stageProps: List<StagePropViewModel> = emptyList()

    fun getId(): Int? {
        return id
    }

    fun setId(id: Int?): StageViewModel {
        this.id = id
        return this
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String?): StageViewModel {
        this.name = name
        return this
    }

    fun getWidth(): Int? {
        return width
    }

    fun setWidth(width: Int?): StageViewModel {
        this.width = width
        return this
    }

    fun getHeight(): Int? {
        return height
    }

    fun setHeight(height: Int?): StageViewModel {
        this.height = height
        return this
    }

    fun getStageProps(): List<StagePropViewModel> {
        return stageProps
    }

    fun setStageProps(stageProps: List<StagePropViewModel>): StageViewModel {
        this.stageProps = stageProps
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StageViewModel

        if (id != other.id) return false
        if (name != other.name) return false
        if (width != other.width) return false
        if (height != other.height) return false
        if (stageProps != other.stageProps) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + (width ?: 0)
        result = 31 * result + (height ?: 0)
        result = 31 * result + stageProps.hashCode()
        return result
    }

    override fun toString(): String {
        return "StageViewModel(id=$id, name=$name, width=$width, height=$height, stageProps=$stageProps)"
    }
}
