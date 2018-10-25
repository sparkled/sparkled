package io.sparkled.model.animation.fill

import io.sparkled.model.animation.param.HasParams
import io.sparkled.model.animation.param.Param
import java.util.Objects

class FillType : HasParams {

    private var code: FillTypeCode? = null
    private var name: String? = null
    private var params: List<Param>? = null

    fun getCode(): FillTypeCode {
        return code
    }

    fun setCode(code: FillTypeCode): FillType {
        this.code = code
        return this
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String): FillType {
        this.name = name
        return this
    }

    @Override
    fun getParams(): List<Param> {
        return params
    }

    fun setParams(params: List<Param>): FillType {
        this.params = params
        return this
    }

    @Override
    fun equals(o: Object): Boolean {
        if (this === o) return true
        if (o !is FillType) return false
        val fillType = o
        return code === fillType.code &&
                Objects.equals(name, fillType.name) &&
                Objects.equals(params, fillType.params)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(code, name, params)
    }

    @Override
    fun toString(): String {
        return "FillType{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", params=" + params +
                '}'
    }
}