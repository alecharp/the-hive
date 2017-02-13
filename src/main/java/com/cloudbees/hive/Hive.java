package com.cloudbees.hive;

import com.cloudbees.hive.model.Bee;
import com.cloudbees.hive.model.Location;
import com.cloudbees.hive.service.BeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author Adrien Lecharpentier
 */
@SpringBootApplication
public class Hive {
    public static void main(String[] args) {
        SpringApplication.run(Hive.class, args);
    }

    private final BeeService beeService;
    @Autowired
    public Hive(BeeService beeService) {
        this.beeService = beeService;
    }
    
    @Bean
    public CommandLineRunner init () {
        return args -> {

            this.beeService.addBee(new Bee("Martin B.", new Location(48.856614, 2.352222)));
            this.beeService.addBee(new Bee("Hans D.", new Location(52.520007, 13.404954)));
            this.beeService.addBee(new Bee("John S.", new Location(40.712784, -74.005941)));
        };
    }
}

