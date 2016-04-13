package net.chrisparton.xmas.entity;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class AnimationEffectTypeParam {

    public static final AnimationEffectTypeParam COLOUR = new AnimationEffectTypeParam("COLOUR", "Colour");
    public static final AnimationEffectTypeParam MULTI_COLOUR = new AnimationEffectTypeParam("COLOURS", "Colours");
    public static final List<AnimationEffectTypeParam> PARAMS = Arrays.asList(
            COLOUR, MULTI_COLOUR
    );

    private String code;
    private String name;

    public AnimationEffectTypeParam(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
