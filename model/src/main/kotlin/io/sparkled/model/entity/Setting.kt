package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "setting")
public class Setting {

    @Id
    @Column(name = "code")
    private String code;

    @Basic
    @Column(name = "value")
    private String value;

    public String getCode() {
        return code;
    }

    public Setting setCode(String code) {
        this.code = code;
        return this;
    }

    public String getValue() {
        return value;
    }

    public Setting setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Setting setting = (Setting) o;
        return Objects.equals(code, setting.code) &&
                Objects.equals(value, setting.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, value);
    }

    @Override
    public String toString() {
        return "Setting{" +
                "code='" + code + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
