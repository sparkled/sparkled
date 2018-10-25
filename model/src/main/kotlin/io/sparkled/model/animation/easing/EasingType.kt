package io.sparkled.model.animation.easing

import io.sparkled.model.animation.param.HasParams
import io.sparkled.model.animation.param.Param
import java.util.Objects

class EasingType : HasParams {

    private var code: EasingTypeCode? = null
    private var name: String? = null
    private var params: List<Param>? = null

    fun getCode(): EasingTypeCode {
        return code
    }

    fun setCode(code: EasingTypeCode): EasingType {
        this.code = code
        return this
    }

    fun getName(): String {
        return name
    }

    fun setName(name: String): EasingType {
        this.name = name
        return this
    }

    @Override
    fun getParams(): List<Param> {
        return params
    }

    fun setParams(params: List<Param>): EasingType {
        this.params = params
        return this
    }

    @Override
    fun equals(o: Object): Boolean {
        if (this === o) return true
        if (o !is EasingType) return false
        val easingType = o
        return code === easingType.code &&
                Objects.equals(name, easingType.name) &&
                Objects.equals(params, easingType.params)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(code, name, params)
    }

    @Override
    fun toString(): String {
        return "EasingType{" +
                "code=" + code +
                ", name='" + name + '\'' +
                ", params=" + params +
                '}'
    }
}
