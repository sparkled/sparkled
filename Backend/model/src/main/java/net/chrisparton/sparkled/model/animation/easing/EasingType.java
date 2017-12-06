package net.chrisparton.sparkled.model.animation.easing;

import java.util.Objects;

public class EasingType {

    private EasingTypeCode code;
    private String name;

    public EasingTypeCode getCode() {
        return code;
    }

    public EasingType setCode(EasingTypeCode code) {
        this.code = code;
        return this;
    }

    public String getName() {
        return name;
    }

    public EasingType setName(String name) {
        this.name = name;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof EasingType)) return false;
        EasingType that = (EasingType) o;
        return code == that.code &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name);
    }

    @Override
    public String toString() {
        return "EasingType{" +
                "code=" + code +
                ", name='" + name + '\'' +
                '}';
    }
}
