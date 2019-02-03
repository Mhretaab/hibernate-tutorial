package com.tutorial.hibernate.models;

import org.hibernate.annotations.SortComparator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

@Entity
@Table(name="honey", uniqueConstraints={@UniqueConstraint(columnNames={"id"})})
@SequenceGenerator(name = "honey_seq", sequenceName = "honey_id_seq")//hibernate name and database name of seq generator
public class Honey implements Serializable {

    private static final long serialVersionUID = 950600335439388061L;
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="honey_seq")//use GenerationType.AUTO if db does not support sequence
    private Integer id; //mandatory
    private String name;
    private String taste;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)//(mappedBy="honey")
    @JoinColumn(name = "honey_id")
    @SortComparator(BeeComparator.class)
    private Set<Bee> bees = new TreeSet<>(new BeeComparator());;

    public Honey() {//mandatory
    }

    public Honey(String name, String taste) {
        this.name = name;
        this.taste = taste;
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

    public String getTaste() {
        return taste;
    }

    public void setTaste(String taste) {
        this.taste = taste;
    }

    public Set<Bee> getBees() {
        return bees;
    }

    public void setBees(Set<Bee> bees) {
        this.bees = bees;
    }

    @Override
    public String toString() {//for debuging
        return "Honey: " + getId() + " Name: " + getName() + " Taste: " + getTaste();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Honey honey = (Honey) o;
        return Objects.equals(id, honey.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }
}
