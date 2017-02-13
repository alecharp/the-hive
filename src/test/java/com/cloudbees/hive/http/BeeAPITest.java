package com.cloudbees.hive.http;

import com.cloudbees.hive.model.Bee;
import com.cloudbees.hive.model.Location;
import com.cloudbees.hive.service.BeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Adrien Lecharpentier
 */
@RunWith(SpringRunner.class)
@WebMvcTest(BeeAPI.class)
public class BeeAPITest {

    @Autowired private MockMvc mvc;
    @MockBean private BeeService beeService;

    @Test
    public void shouldBeAbleToGetTheHiveMember() throws Exception {
        List<Bee> hive = Arrays.asList(
            new Bee("Adrien L.", new Location(0, 0)),
            new Bee("Arnaud H.", new Location(45, 45))
        );

        given(this.beeService.getAll())
            .willReturn(hive);

        this.mvc.perform(get("/api/hive"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[*].name", Matchers.contains("Adrien L.", "Arnaud H.")))
            .andReturn();
    }

    @Test
    public void shouldBeAbleToCreateANewBee() throws Exception {
        Bee maya = new Bee("John D.", new Location(0, 0));
        given(this.beeService.addBee(any(Bee.class))).willReturn(maya);

        ObjectMapper objectMapper = new ObjectMapper();

        this.mvc.perform(post("/api/bee")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(maya)))
            .andExpect(status().isCreated());
        // TODO test that the header "Location" is set when storing bees into DB
    }
}