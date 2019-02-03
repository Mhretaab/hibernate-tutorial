package com.tutorial.hibernate.models;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Objects;

@Entity
@Table(name = "bee", uniqueConstraints={@UniqueConstraint(columnNames={"id"})})
//@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "honey")
public class Bee implements Serializable {
    private static final long serialVersionUID = -8644055749514146694L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bee_gen")//use GenerationType.AUTO if db does not support sequence
    @SequenceGenerator(name = "bee_gen", sequenceName = "bee_id_seq")
    private Integer id;
    private String name;

    @ManyToOne
    @JoinColumn
    private Honey honey;

    public Bee() {
    }

    public Bee(String name) {
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Honey getHoney() {
        return honey;
    }

    public void setHoney(Honey honey) {
        this.honey = honey;
    }

    @Override
    public String toString() {
        return MessageFormat.format("{0}: id={1}, name={2}", new Object[] {getClass().getSimpleName(), id, name });
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bee bee = (Bee) o;
        return Objects.equals(id, bee.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
