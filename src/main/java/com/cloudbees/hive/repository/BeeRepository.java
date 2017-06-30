package com.cloudbees.hive.repository;

import com.cloudbees.hive.model.Bee;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Adrien Lecharpentier
 */
@Repository
public interface BeeRepository extends CrudRepository<Bee, String> {
    @Query("select distinct(location) from Bee b")
    Iterable<String> findAllDistinctLocation();
}
