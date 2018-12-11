package io.sparkled.model.animation.effect

import io.sparkled.model.animation.param.Param

class EffectType {

    private var code: EffectTypeCode? = null
    private var name: String? = null
    private var params: List<Param> = emptyList()

    fun getCode(): EffectTypeCode? {
        return code
    }

    fun setCode(code: EffectTypeCode): EffectType {
        this.code = code
        return this
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String): EffectType {
        this.name = name
        return this
    }

    fun getParams(): List<Param> {
        return params
    }

    fun setParams(params: List<Param>): EffectType {
        this.params = params
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as EffectType

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
        return "EffectType(code=$code, name=$name, params=$params)"
    }
}
