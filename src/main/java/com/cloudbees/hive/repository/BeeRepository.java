package com.cloudbees.hive.repository;

import com.cloudbees.hive.model.Bee;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Adrien Lecharpentier
 */
@Repository
public interface BeeRepository extends CrudRepository<Bee, String> {
    Optional<Bee> findByEmail(String email);
}
