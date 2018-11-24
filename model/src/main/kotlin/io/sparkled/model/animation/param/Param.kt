package io.sparkled.model.animation.param

class Param {

    private var name: ParamName? = null
    private var type: ParamType? = null
    private var value: List<String?> = emptyList()

    fun getName(): ParamName? {
        return name
    }

    fun setName(name: ParamName): Param {
        this.name = name
        return this
    }

    fun getType(): ParamType? {
        return type
    }

    fun setType(type: ParamType): Param {
        this.type = type
        return this
    }

    fun getValue(): List<String?> {
        return value
    }

    fun setValue(value: List<String>): Param {
        this.value = value
        return this
    }

    fun setValue(value: Any): Param {
        this.value = listOf(value.toString())
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Param

        if (name != other.name) return false
        if (type != other.type) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (type?.hashCode() ?: 0)
        result = 31 * result + value.hashCode()
        return result
    }

    override fun toString(): String {
        return "Param(name=$name, type=$type, value=$value)"
    }
}
