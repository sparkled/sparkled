package io.sparkled.model.entity;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "stage")
public class Stage {

    private Integer id;
    private String svg;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Basic
    @Column(name = "svg", columnDefinition = "text", nullable = false)
    public String getSvg() {
        return svg;
    }

    public void setSvg(String svg) {
        this.svg = svg;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Stage svg = (Stage) o;
        return Objects.equals(id, svg.id) &&
                Objects.equals(this.svg, svg.svg);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, svg);
    }
}
