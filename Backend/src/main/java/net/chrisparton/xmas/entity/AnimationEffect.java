package net.chrisparton.xmas.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "animation_effect", schema = "xmas", catalog = "postgres")
public class AnimationEffect {

    private String code;
    private String name;
    private String imagePath;

    @Id
    @Basic
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

    @Basic
    @Column(name = "image_path", nullable = false, length = 128)
    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnimationEffect)) return false;
        AnimationEffect that = (AnimationEffect) o;
        return Objects.equals(code, that.code) &&
                Objects.equals(name, that.name) &&
                Objects.equals(imagePath, that.imagePath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, name, imagePath);
    }
}
