package com.cloudbees.hive.repository;

import com.cloudbees.hive.model.Bee;
import com.cloudbees.hive.model.Location;
import org.junit.jupiter.api.Test;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Adrien Lecharpentier
 */
@RunWith(JUnitPlatform.class)
class BeeRepositoryTest {
    @Test
    public void shouldBeAbleToReturnWhatWasJustAdded() {
        Bee maya = new Bee("Maya the Bee", new Location(0, 0));
        Bee result = new BeeRepository().addBee(maya);
        assertThat(result).isEqualTo(maya);
    }
}