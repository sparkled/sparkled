package net.chrisparton.xmas.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "animation_effect_type_param", schema = "xmas", catalog = "postgres")
public class AnimationEffectTypeParam {

    private AnimationEffectParamTypeCode code;
    private String name;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "code", nullable = false, length = 64)
    public AnimationEffectParamTypeCode getCode() {
        return code;
    }

    public void setCode(AnimationEffectParamTypeCode code) {
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
        if (!(o instanceof AnimationEffectTypeParam)) return false;
        AnimationEffectTypeParam that = (AnimationEffectTypeParam) o;
        return Objects.equals(code, that.code) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name);
    }
}
