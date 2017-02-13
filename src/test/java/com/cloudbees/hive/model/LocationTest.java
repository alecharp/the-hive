package com.cloudbees.hive.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(JUnitPlatform.class)
public class LocationTest {
    @Test
    @DisplayName("The latitude should be between -90 and 90 degree")
    public void latitudeShouldBeBetweenAbs90() {
        assertAll("latitude",
            () -> assertThrows(IllegalArgumentException.class, () -> new Location(-91, 0)),
            () -> assertThrows(IllegalArgumentException.class, () -> new Location(91, 0)),
            () -> assertNotNull(new Location(-90, 0)),
            () -> assertNotNull(new Location(90, 0)),
            () -> assertNotNull(new Location(0, 0))
        );
    }

    @Test
    @DisplayName("The Longitude should be between -180 and 180 degree")
    public void longitudeShouldBeBetweenAbs180() {
        assertAll("longitude",
            () -> assertThrows(IllegalArgumentException.class, () -> new Location(0, 181)),
            () -> assertThrows(IllegalArgumentException.class, () -> new Location(0, -181)),
            () -> assertNotNull(new Location(0, 180)),
            () -> assertNotNull(new Location(0, -180)),
            () -> assertNotNull(new Location(0, 0))
        );
    }

    @Test
    public void shouldBeEqualsOnSameLatitudeAndLongitude() {
        assertAll("equals",
            () -> {
                Location location = new Location(45, 45);
                assertTrue(location.equals(location));
            },
            () -> assertTrue(new Location(0.0, 0.0).equals(new Location(0, 0))),
            () -> assertFalse(new Location(0,0).equals(new Location(0.000001, 0))),
            () -> assertFalse(new Location(0,0).equals(new Location(0, 0.000001)))
        );
    }
}
