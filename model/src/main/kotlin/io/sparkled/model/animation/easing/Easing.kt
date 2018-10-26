package io.sparkled.model.animation.easing

import io.sparkled.model.animation.param.HasParams
import io.sparkled.model.animation.param.Param
import java.util.*

class Easing : HasParams {

    private var type: EasingTypeCode? = null
    private var params: List<Param> = ArrayList()

    fun getType(): EasingTypeCode {
        return type!!
    }

    fun setType(type: EasingTypeCode): Easing {
        this.type = type
        return this
    }

    override fun getParams(): List<Param> {
        return this.params
    }

    fun setParams(params: List<Param>): Easing {
        this.params = params
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Easing

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
        return "Easing(type=$type, params=$params)"
    }
}