package com.cloudbees.hive.repository;

import com.cloudbees.hive.model.Bee;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Adrien Lecharpentier
 */
@Repository
public class BeeRepository {
    private final ArrayList<Bee> hive = new ArrayList<>();
    private static final Object monitor = new Object();

    public void addBee(Bee bee) {
        synchronized (monitor) {
            if(!hive.contains(bee)) {
                hive.add(bee);
            }
        }
    }

    public List<Bee> getHive() {
        synchronized (monitor) {
            return Collections.unmodifiableList(hive);
        }
    }
}
