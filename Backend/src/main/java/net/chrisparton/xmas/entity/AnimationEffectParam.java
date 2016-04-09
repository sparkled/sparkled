package net.chrisparton.xmas.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "animation_effect_param", schema = "xmas", catalog = "postgres")
public class AnimationEffectParam {

    private AnimationEffectParamCode code;
    private String name;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "code", nullable = false, length = 64)
    public AnimationEffectParamCode getCode() {
        return code;
    }

    public void setCode(AnimationEffectParamCode code) {
        this.code = code;
    }

    @Basic
    @Column(name = "name", nullable = false, length = 64)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnimationEffectParam)) return false;
        AnimationEffectParam that = (AnimationEffectParam) o;
        return Objects.equals(code, that.code) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name);
    }
}
