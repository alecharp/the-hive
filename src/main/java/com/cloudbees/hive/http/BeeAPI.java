package com.cloudbees.hive.http;

import com.cloudbees.hive.model.Bee;
import com.cloudbees.hive.service.BeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

/**
 * @author Adrien Lecharpentier
 */
@RestController
public class BeeAPI {
    private final BeeService service;

    @Autowired
    public BeeAPI(BeeService service) {
        this.service = service;
    }

    @GetMapping(value = "/api/hive")
    public Iterable<Bee> getHive() {
        return this.service.all();
    }

    @GetMapping(value = "/api/bee/{id}")
    public ResponseEntity<Bee> getBee(@PathVariable String id) {
        return this.service.byId(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping(value = "/api/bee")
    public ResponseEntity<Bee> createBee(@RequestBody Bee bee) {
        Bee added = this.service.add(bee);
        return ResponseEntity.created(URI.create("/api/bee/" + added.getId())).body(added);
    }
}
