package com.cloudbees.hive.http;

import com.cloudbees.hive.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Adrien Lecharpentier
 */
@RestController
public class LocationAPI {
    private final LocationService service;

    @Autowired
    public LocationAPI(LocationService service) {
        this.service = service;
    }

    @GetMapping(value = "/api/locations")
    public Iterable<String> getLocations() {
        return service.all();
    }
}
