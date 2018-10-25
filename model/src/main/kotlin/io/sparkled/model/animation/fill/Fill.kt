package io.sparkled.model.animation.fill

import io.sparkled.model.animation.param.HasParams
import io.sparkled.model.animation.param.Param

class Fill : HasParams {

    private var type: FillTypeCode? = null
    private var params: List<Param> = ArrayList()

    fun getType(): FillTypeCode? {
        return type
    }

    fun setType(type: FillTypeCode): Fill {
        this.type = type
        return this
    }

    override fun getParams(): List<Param> {
        return params
    }

    fun setParams(params: List<Param>): Fill {
        this.params = params
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Fill

        if (type != other.type) return false
        if (params != other.params) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type?.hashCode() ?: 0
        result = 31 * result + params.hashCode()
        return result
    }

    override fun toString(): String {
        return "Fill(type=$type, params=$params)"
    }
}