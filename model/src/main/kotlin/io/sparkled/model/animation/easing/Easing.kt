package io.sparkled.model.animation.easing

import io.sparkled.model.animation.param.HasParams
import io.sparkled.model.animation.param.Param

import java.util.Arrays
import java.util.Objects

class Easing : HasParams {

    private var type: EasingTypeCode? = null
    private var params: List<Param>? = null

    fun getType(): EasingTypeCode {
        return type
    }

    fun setType(type: EasingTypeCode): Easing {
        this.type = type
        return this
    }

    @Override
    fun getParams(): List<Param> {
        return params
    }

    fun setParams(vararg params: Param): Easing {
        return setParams(Arrays.asList(params))
    }

    fun setParams(params: List<Param>): Easing {
        this.params = params
        return this
    }

    @Override
    fun equals(o: Object): Boolean {
        if (this === o) return true
        if (o !is Easing) return false
        val easing = o
        return type === easing.type && Objects.equals(params, easing.params)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(type, params)
    }

    @Override
    fun toString(): String {
        return "Easing{" +
                "type=" + type +
                ", params=" + params +
                '}'
    }
}