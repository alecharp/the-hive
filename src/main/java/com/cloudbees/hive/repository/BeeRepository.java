package com.cloudbees.hive.repository;

import com.cloudbees.hive.model.Bee;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Adrien Lecharpentier
 */
@Repository
public class BeeRepository {
    private final ConcurrentHashMap<String, Bee> hive = new ConcurrentHashMap<>();

    public Bee addBee(Bee bee) {
        hive.put(bee.getId(), bee);
        return bee;
    }

    public Collection<Bee> getHive() {
        return hive.values();
    }
}
