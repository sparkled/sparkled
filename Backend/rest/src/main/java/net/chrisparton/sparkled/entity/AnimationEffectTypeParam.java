package net.chrisparton.sparkled.entity;

import java.util.Objects;

public class AnimationEffectTypeParam {

    private String code;
    private String name;

    AnimationEffectTypeParam(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof AnimationEffectTypeParam)) {
            return false;
        }

        AnimationEffectTypeParam that = (AnimationEffectTypeParam) o;
        return Objects.equals(name, that.name) &&
                Objects.equals(code, that.code);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code);
    }
}
