package io.sparkled.model.entity

import javax.persistence.Basic
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "setting")
class Setting {

    @Id
    @Column(name = "code")
    private var code: String? = null

    @Basic
    @Column(name = "value")
    private var value: String? = null

    fun getCode(): String? {
        return code
    }

    fun setCode(code: String?): Setting {
        this.code = code
        return this
    }

    fun getValue(): String? {
        return value
    }

    fun setValue(value: String?): Setting {
        this.value = value
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Setting

        if (code != other.code) return false
        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = code?.hashCode() ?: 0
        result = 31 * result + (value?.hashCode() ?: 0)
        return result
    }

    override fun toString(): String {
        return "Setting(code=$code, value=$value)"
    }
}
