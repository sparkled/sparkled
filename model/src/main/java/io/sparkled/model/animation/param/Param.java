package io.sparkled.model.animation.param;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Param {

    private String name;
    private ParamType type;
    private List<String> value = new ArrayList<>();

    public String getName() {
        return name;
    }

    public Param setName(String name) {
        this.name = name;
        return this;
    }

    public ParamType getType() {
        return type;
    }

    public Param setType(ParamType type) {
        this.type = type;
        return this;
    }

    public List<String> getValue() {
        return value;
    }

    public Param setValue(Object value) {
        this.value = new ArrayList<>();
        this.value.add(String.valueOf(value));
        return this;
    }

    public Param setValue(List<String> value) {
        this.value = value;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Param)) return false;
        Param that = (Param) o;
        return Objects.equals(name, that.name) &&
                type == that.type &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type, value);
    }

    @Override
    public String toString() {
        return "Param{" +
                "name='" + name + '\'' +
                ", type=" + type +
                ", value=" + value +
                '}';
    }
}
