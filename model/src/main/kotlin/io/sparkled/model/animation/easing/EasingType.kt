package io.sparkled.model.animation.easing

import io.sparkled.model.animation.param.HasParams
import io.sparkled.model.animation.param.Param

class EasingType : HasParams {

    private var code: EasingTypeCode? = null
    private var name: String? = null
    private var params: List<Param> = emptyList()

    fun getCode(): EasingTypeCode? {
        return code
    }

    fun setCode(code: EasingTypeCode): EasingType {
        this.code = code
        return this
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String): EasingType {
        this.name = name
        return this
    }

    override fun getParams(): List<Param> {
        return params
    }

    fun setParams(params: List<Param>): EasingType {
        this.params = params
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EasingType

        if (code != other.code) return false
        if (name != other.name) return false
        if (params != other.params) return false

        return true
    }

    override fun hashCode(): Int {
        var result = code?.hashCode() ?: 0
        result = 31 * result + (name?.hashCode() ?: 0)
        result = 31 * result + params.hashCode()
        return result
    }

    override fun toString(): String {
        return "EasingType(code=$code, name=$name, params=$params)"
    }
}
