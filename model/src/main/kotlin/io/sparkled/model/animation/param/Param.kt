package io.sparkled.model.animation.param

import java.util.ArrayList
import java.util.Objects

class Param {

    private var name: ParamName? = null
    private var type: ParamType? = null
    private var value: List<String> = ArrayList()

    fun getName(): ParamName {
        return name
    }

    fun setName(name: ParamName): Param {
        this.name = name
        return this
    }

    fun getType(): ParamType {
        return type
    }

    fun setType(type: ParamType): Param {
        this.type = type
        return this
    }

    fun getValue(): List<String> {
        return value
    }

    fun setValue(value: List<String>): Param {
        this.value = value
        return this
    }

    fun setValue(value: Object): Param {
        this.value = ArrayList()
        this.value.add(String.valueOf(value))
        return this
    }

    @Override
    fun equals(o: Object): Boolean {
        if (this === o) return true
        if (o !is Param) return false
        val that = o
        return Objects.equals(name, that.name) &&
                type === that.type &&
                Objects.equals(value, that.value)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(name, type, value)
    }

    @Override
    fun toString(): String {
        return "Param{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", value=" + value +
                '}'
    }
}
