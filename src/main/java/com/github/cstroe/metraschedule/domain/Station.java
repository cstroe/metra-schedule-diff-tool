package com.github.cstroe.metraschedule.domain;

public class Station {
    public final int id;
    public final String name;

    private Station(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public static Station of(String id, String name) {
        return new Station(Integer.parseInt(id), name);
    }

    public static Station of(int id, String name) {
        return new Station(id, name);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(id);
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Station) {
            Station o = (Station) obj;
            return id == o.id;
        }
        return false;
    }
}
