package io.sparkled.model.animation.fill

import io.sparkled.model.animation.param.HasParams
import io.sparkled.model.animation.param.Param

class FillType : HasParams {

    private var code: FillTypeCode? = null
    private var name: String? = null
    private var params: List<Param> = emptyList()

    fun getCode(): FillTypeCode? {
        return code
    }

    fun setCode(code: FillTypeCode): FillType {
        this.code = code
        return this
    }

    fun getName(): String? {
        return name
    }

    fun setName(name: String): FillType {
        this.name = name
        return this
    }

    override fun getParams(): List<Param> {
        return params
    }

    fun setParams(params: List<Param>): FillType {
        this.params = params
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FillType

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
        return "FillType(code=$code, name=$name, params=$params)"
    }
}