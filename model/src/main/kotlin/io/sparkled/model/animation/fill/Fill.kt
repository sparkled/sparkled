package io.sparkled.model.animation.fill

import io.sparkled.model.animation.param.HasParams
import io.sparkled.model.animation.param.Param

import java.util.Arrays
import java.util.Objects

class Fill : HasParams {

    private var type: FillTypeCode? = null
    private var params: List<Param>? = null

    fun getType(): FillTypeCode {
        return type
    }

    fun setType(type: FillTypeCode): Fill {
        this.type = type
        return this
    }

    @Override
    fun getParams(): List<Param> {
        return params
    }

    fun setParams(vararg params: Param): Fill {
        return setParams(Arrays.asList(params))
    }

    fun setParams(params: List<Param>): Fill {
        this.params = params
        return this
    }

    @Override
    fun equals(o: Object): Boolean {
        if (this === o) return true
        if (o !is Fill) return false
        val fill = o
        return type === fill.type && Objects.equals(params, fill.params)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(type, params)
    }

    @Override
    fun toString(): String {
        return "Fill{" +
                "type=" + type +
                ", params=" + params +
                '}'
    }
}