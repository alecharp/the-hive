package com.cloudbees.hive;

import com.cloudbees.hive.model.Bee;
import com.cloudbees.hive.repository.BeeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.web.context.WebApplicationContext;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

/**
 * @author Adrien Lecharpentier
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HiveIT {

    @Autowired private WebApplicationContext context;
    private WebDriver webDriver;
    @MockBean private BeeRepository beeRepository;

    @Before
    public void setup() {
        webDriver = MockMvcHtmlUnitDriverBuilder
            .webAppContextSetup(context)
            .useMockMvcForHosts("localhost")
            .javascriptEnabled(true)
            .build();

        when(beeRepository.findAll()).thenReturn(
            Arrays.asList(
                new Bee("John D.", "New York City, US"),
                new Bee("M. X", "Paris, France")
            )
        );
    }

    @Test
    public void shouldHaveTheCorrectTitle() {
        webDriver.get("http://localhost/index.html");
        assertThat(webDriver.getTitle()).isEqualTo("The Hive");
    }
}
