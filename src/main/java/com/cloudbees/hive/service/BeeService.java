package com.cloudbees.hive.service;

import com.cloudbees.hive.model.Bee;
import com.cloudbees.hive.repository.BeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * @author Adrien Lecharpentier
 */
@Service
public class BeeService {
    private final BeeRepository repository;

    @Autowired
    public BeeService(BeeRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public Bee add(Bee bee) {
        return this.repository.save(bee);
    }

    @Transactional(readOnly = true)
    public Iterable<Bee> all() {
        return this.repository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Bee> byId(String id) {
        return Optional.of(this.repository.findOne(id));
    }

    @Transactional(readOnly = true)
    protected Optional<Bee> byEmail(String email) {
        return this.repository.findByEmail(email);
    }

    public List<GrantedAuthority> authenticate(String email, String name) {
        byEmail(email)
            .orElse(add(new Bee(name, email)));
        return Collections.singletonList(new SimpleGrantedAuthority("USER"));
    }
}
