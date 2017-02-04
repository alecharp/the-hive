package com.cloudbees.hive.http;

import com.cloudbees.hive.model.Bee;
import com.cloudbees.hive.service.BeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping(value = "/api/bees")
    public List<Bee> getHive() {
        return this.beeService.getAll();
    }

    @PostMapping(value = "/api/bee")
    @ResponseStatus(value = HttpStatus.CREATED)
    public void newBee(@RequestBody Bee bee) {
        this.beeService.addBee(bee);
    }
}
