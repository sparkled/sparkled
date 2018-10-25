package io.sparkled.model.entity

import javax.persistence.*
import java.util.Objects

@Entity
@Table(name = "setting")
class Setting {

    @Id
    @Column(name = "code")
    private var code: String? = null

    @Basic
    @Column(name = "value")
    private var value: String? = null

    fun getCode(): String {
        return code
    }

    fun setCode(code: String): Setting {
        this.code = code
        return this
    }

    fun getValue(): String {
        return value
    }

    fun setValue(value: String): Setting {
        this.value = value
        return this
    }

    @Override
    fun equals(o: Object?): Boolean {
        if (this === o) return true
        if (o == null || getClass() !== o!!.getClass()) return false
        val setting = o
        return Objects.equals(code, setting!!.code) && Objects.equals(value, setting.value)
    }

    @Override
    fun hashCode(): Int {
        return Objects.hash(code, value)
    }

    @Override
    fun toString(): String {
        return "Setting{" +
                "code='" + code + '\'' +
                ", value='" + value + '\'' +
                '}'
    }
}
