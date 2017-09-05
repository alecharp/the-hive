package com.cloudbees.hive;

import com.cloudbees.hive.service.BeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.htmlunit.webdriver.MockMvcHtmlUnitDriverBuilder;
import org.springframework.web.context.WebApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Adrien Lecharpentier
 */
@RunWith(SpringRunner.class)
@WebMvcTest
public class HiveAppIT {

    @Autowired private WebApplicationContext context;
    private WebDriver webDriver;
    @MockBean private BeeService beeService;

    @Before
    public void setup() {
        webDriver = MockMvcHtmlUnitDriverBuilder
            .webAppContextSetup(context)
            .useMockMvcForHosts("localhost")
            .javascriptEnabled(false)
            .build();
    }

    @Test
    public void shouldHaveTheCorrectTitle() {
        webDriver.get("http://localhost/index.html");
        assertThat(webDriver.getTitle()).isEqualTo("The Hive");
    }
}
