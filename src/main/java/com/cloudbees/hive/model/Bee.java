package com.cloudbees.hive.model;

import java.util.Objects;
import java.util.UUID;

/**
 * @author Adrien Lecharpentier
 */
public class Bee {
    private String id;

    private String name;
    private Location location;

    private Bee() {
        this.id = UUID.randomUUID().toString();
    }

    public Bee(String name, Location location) {
        this();
        this.name = name;
        this.location = location;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Bee bee = (Bee) o;
        return Objects.equals(name, bee.name) &&
            Objects.equals(location, bee.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
