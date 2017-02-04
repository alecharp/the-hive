package com.cloudbees.hive.http;

import com.cloudbees.hive.model.Bee;
import com.cloudbees.hive.service.BeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
