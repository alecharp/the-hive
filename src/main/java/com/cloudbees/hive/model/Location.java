package com.cloudbees.hive.model;

import java.util.Objects;

/**
 * @author Adrien Lecharpentier
 */
public class Location {
    private double longitude, latitude;

    private Location() {
    }

    public Location(double latitude, double longitude) {
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location residence = (Location) o;
        return Double.compare(residence.longitude, longitude) == 0 &&
            Double.compare(residence.latitude, latitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(longitude, latitude);
    }
}
