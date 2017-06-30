package com.cloudbees.hive.http;

import com.cloudbees.hive.model.Bee;
import com.cloudbees.hive.service.BeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collection;

/**
 * @author Adrien Lecharpentier
 */
@RestController
public class BeeAPI {
    private final BeeService beeService;

    @Autowired
    public BeeAPI(BeeService beeService) {
        this.beeService = beeService;
    }

    @GetMapping(value = "/api/hive")
    public Iterable<Bee> getHive() {
        return this.beeService.getAll();
    }

    @PostMapping(value = "/api/bee")
    public ResponseEntity<Bee> createBee(@RequestBody Bee bee) throws URISyntaxException {
        Bee added = this.beeService.addBee(bee);
        return ResponseEntity.created(new URI("/api/bee/" + added.getId())).body(added);
    }
}
