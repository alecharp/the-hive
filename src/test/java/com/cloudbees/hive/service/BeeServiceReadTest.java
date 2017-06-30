package com.cloudbees.hive.service;

import com.cloudbees.hive.model.Bee;
import com.cloudbees.hive.repository.BeeRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

/**
 * @author Adrien Lecharpentier
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class BeeServiceReadTest {

    @MockBean private BeeRepository beeRepository;
    @Autowired private BeeService beeService;

    @Test
    public void shouldBeAbleToGetTheHive() {
        List<Bee> expectedHive = Arrays.asList(
            new Bee("Adrien L.", "Paris, France"),
            new Bee("Arnaud H.", "Paris, France")
        );

        given(this.beeRepository.findAll()).willReturn(expectedHive);
        assertThat(this.beeService.getAll()).containsExactlyElementsOf(expectedHive);
    }
}
