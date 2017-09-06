package com.cloudbees.hive.model;

import org.junit.Test;

import java.lang.reflect.Field;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class BeeTest {
    @Test
    public void shouldBeAbleToMergeTwoBee() throws Exception {
        Bee maya = new Bee("Maya", "maya@bee.fr");

        Field id = maya.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(maya, UUID.randomUUID().toString());

        Bee willy = new Bee("Willy", "willy@bee.fr", 15, 45);
        id.set(willy, UUID.randomUUID().toString());

        Bee merge = maya.merge(willy);

        assertThat(merge.getId()).isEqualTo(maya.getId()).isNotEqualTo(willy.getId());
        assertThat(merge.getName()).isEqualTo(willy.getName());
        assertThat(merge.getEmail()).isEqualTo(willy.getEmail());
        assertThat(merge.getLatitude()).isEqualTo(willy.getLatitude());
        assertThat(merge.getLongitude()).isEqualTo(willy.getLongitude());
    }
}
