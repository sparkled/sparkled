package net.chrisparton.xmas.entity;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "animation_effect_type", schema = "xmas", catalog = "postgres")
public class AnimationEffectType {

    private String code;
    private String name;
    private List<AnimationEffectTypeParam> parameters;

    @Id
    @Column(name = "code", nullable = false, length = 64)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "animation_effect_type_param_map",
            schema = "xmas",
            joinColumns = @JoinColumn(name = "animation_effect_type_code", nullable = false, updatable = false),
            inverseJoinColumns = @JoinColumn(name = "animation_effect_type_param_code", nullable = false, updatable = false)
    )
    public List<AnimationEffectTypeParam> getParameters() {
        return parameters;
    }

    public void setParameters(List<AnimationEffectTypeParam> parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnimationEffectType)) return false;
        AnimationEffectType that = (AnimationEffectType) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name);
    }
}
