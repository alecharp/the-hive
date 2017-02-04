package com.cloudbees.hive.model;

import java.util.Objects;

/**
 * @author Adrien Lecharpentier
 */
public class Bee {
    private final String name;
    private final Location location;

    public Bee(String name, Location location) {
        this.name = name;
        this.location = location;
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
