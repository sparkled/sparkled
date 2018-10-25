package io.sparkled.viewmodel.stage

import io.sparkled.viewmodel.ViewModel
import io.sparkled.viewmodel.stage.prop.StagePropViewModel

import java.util.ArrayList
import java.util.Objects

class StageViewModel : ViewModel {
    private var id: Integer? = null
    private var name: String? = null
    private var width: Integer? = null
    private var height: Integer? = null
    private var stageProps: List<StagePropViewModel> = ArrayList()

    fun getId(): Integer {
        return id
    }

    fun setId(id: Integer): StageViewModel {
        this.id = id
        return this
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String): StageViewModel {
        this.name = name
        return this
    }

    fun getWidth(): Integer {
        return width
    }

    fun setWidth(width: Integer): StageViewModel {
        this.width = width
        return this
    }

    fun getHeight(): Integer {
        return height
    }

    fun setHeight(height: Integer): StageViewModel {
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

    @Override
    fun equals(o: Object?): Boolean {
        if (this === o) return true
        if (o == null || getClass() !== o!!.getClass()) return false
        val that = o
        return Objects.equals(id, that!!.id) &&
                Objects.equals(name, that.name) &&
                Objects.equals(width, that.width) &&
                Objects.equals(height, that.height) &&
                Objects.equals(stageProps, that.stageProps)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(id, name, width, height, stageProps)
    }

    @Override
    fun toString(): String {
        return "StageViewModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", width=" + width +
                ", height=" + height +
                ", stageProps=" + stageProps +
                '}'
    }
}
