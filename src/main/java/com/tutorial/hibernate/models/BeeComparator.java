package com.tutorial.hibernate.models;

import java.util.Comparator;

public class BeeComparator implements Comparator<Bee>{
    @Override
    public int compare(Bee o1, Bee o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
