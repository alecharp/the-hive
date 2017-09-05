package com.cloudbees.hive.http;

import com.cloudbees.hive.model.Bee;
import com.cloudbees.hive.service.BeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * @author Adrien Lecharpentier
 */
@RestController
@PreAuthorize("isAuthenticated() == true")
public class BeeAPI {
    private final BeeService service;

    @Autowired
    public BeeAPI(BeeService service) {
        this.service = service;
    }

    @GetMapping(value = "/api/hive")
    public Iterable<Bee> hive() {
        return this.service.all();
    }

    @PostMapping(value = "/api/bee")
    @PreAuthorize("#bee.email == principal.email")
    public ResponseEntity<Bee> update(@RequestBody Bee bee) {
        Bee added = this.service.update(bee);
        return ResponseEntity.created(URI.create("/api/bee/" + added.getId())).body(added);
    }
}
