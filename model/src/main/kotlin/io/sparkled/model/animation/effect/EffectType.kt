package io.sparkled.model.animation.effect

import io.sparkled.model.animation.param.HasParams
import io.sparkled.model.animation.param.Param

import java.util.ArrayList
import java.util.Objects

class EffectType : HasParams {

    private var code: EffectTypeCode? = null
    private var name: String? = null
    private var params: List<Param> = ArrayList()

    fun getCode(): EffectTypeCode {
        return code
    }

    fun setCode(code: EffectTypeCode): EffectType {
        this.code = code
        return this
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String): EffectType {
        this.name = name
        return this
    }

    @Override
    fun getParams(): List<Param> {
        return params
    }

    fun setParams(params: List<Param>): EffectType {
        this.params = params
        return this
    }

    @Override
    fun equals(o: Object): Boolean {
        if (this === o) return true
        if (o !is EffectType) return false
        val that = o
        return code === that.code &&
                Objects.equals(name, that.name) &&
                Objects.equals(params, that.params)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(code, name, params)
    }

    @Override
    fun toString(): String {
        return "EffectType{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", params=" + params +
                '}'
    }
}
