package com.cloudbees.hive.http;

import com.cloudbees.hive.model.Bee;
import com.cloudbees.hive.service.BeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
        Bee updated = this.service.update(bee);
        return ResponseEntity.ok(updated);
    }

    @GetMapping(value = "/api/me")
    public Bee me(@AuthenticationPrincipal Bee bee) {
        return bee;
    }
}
