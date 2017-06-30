package com.cloudbees.hive.http;

import com.cloudbees.hive.model.Bee;
import com.cloudbees.hive.service.BeeService;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
            new Bee("Adrien L.", "Paris, France"),
            new Bee("Arnaud H.", "Paris, France")
        );

        given(this.beeService.getAll())
            .willReturn(hive);

        this.mvc.perform(get("/api/hive"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", Matchers.hasSize(hive.size())))
            .andExpect(jsonPath("$[*].id", Matchers.hasItem(Matchers.any(String.class))))
            .andExpect(jsonPath("$[*].name", Matchers.hasItems("Adrien L.", "Arnaud H.")))
            .andReturn();
    }

    @Test
    public void shouldBeAbleToCreateANewBee() throws Exception {
        Bee maya = new Bee("John D.", "New York City, US");
        given(this.beeService.addBee(maya)).willReturn(maya);

        ObjectMapper objectMapper = new ObjectMapper();

        this.mvc.perform(post("/api/bee")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(maya)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location", Matchers.endsWith(maya.getId())));
    }
}
