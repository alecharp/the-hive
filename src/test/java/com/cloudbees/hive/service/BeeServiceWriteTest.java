package com.cloudbees.hive.service;

import com.cloudbees.hive.model.Bee;
import com.cloudbees.hive.model.Location;
import com.cloudbees.hive.repository.BeeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.verify;

/**
 * @author Adrien Lecharpentier
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BeeServiceWriteTest {

    @SpyBean private BeeRepository beeRepository;
    @Autowired private BeeService beeService;

    @Test
    public void shouldBeAbleToStoreAValue() {
        Bee bee = new Bee("Adrien L.", new Location(0, 0));

        this.beeService.addBee(bee);
        verify(this.beeRepository).addBee(bee);
    }
}
