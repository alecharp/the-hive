package com.cloudbees.hive;

import com.cloudbees.hive.model.Bee;
import com.cloudbees.hive.model.Location;
import com.cloudbees.hive.service.BeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class HiveInitializer implements CommandLineRunner {
    private final BeeService beeService;

    @Autowired
    public HiveInitializer(BeeService beeService) {
        this.beeService = beeService;
    }

    @Override
    public void run(String... strings) throws Exception {
        this.beeService.addBee(new Bee("Martin B.", new Location(48.856614, 2.352222)));
        this.beeService.addBee(new Bee("Hans D.", new Location(52.520007, 13.404954)));
        this.beeService.addBee(new Bee("John S.", new Location(40.712784, -74.005941)));
    }
}
