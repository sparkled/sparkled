package net.chrisparton.sparkled.entity;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "stage", schema = "sparkled", catalog = "postgres")
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
    @Column(name = "svg", nullable = false)
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
