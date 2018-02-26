package com.cloudbees.hive.http;

import com.cloudbees.hive.model.Bee;
import com.cloudbees.hive.model.Role;
import com.cloudbees.hive.service.BeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Optional.empty;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;
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

    @Autowired private Gson gson;
    @Autowired private ObjectMapper objectMapper;

    @Test
    public void shouldBeAbleToGetTheHiveMembers() throws Exception {
        List<Bee> hive = Arrays.asList(
            new Bee("Adrien L.", "a@l.fr", 48.864716, 2.349014, "::1", Role.BEE),
            new Bee("Carlos R.", "a@h.fr", 37.392529, -5.994072, "::2", Role.BEE)
        );
        given(this.beeService.all()).willReturn(hive);

        Field id = Bee.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(hive.get(0), UUID.randomUUID().toString());
        id.set(hive.get(1), UUID.randomUUID().toString());

        this.mvc.perform(get("/api/hive"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", Matchers.hasSize(hive.size())))
            .andExpect(jsonPath("$[*].id", Matchers.hasItem(Matchers.any(String.class))))
            .andExpect(jsonPath("$[*].name", Matchers.hasItems("Adrien L.", "Carlos R.")))
            .andExpect(jsonPath("$[*].latitude", Matchers.hasItems(48.864716, 37.392529)))
            .andExpect(jsonPath("$[*].longitude", Matchers.hasItems(2.349014, -5.994072)))
            .andExpect(jsonPath("$[*].location", Matchers.hasItems("::1", "::2")))
            .andReturn();
    }

    @Test
    public void shouldBeAbleToRetrieveOneBee() throws Exception {
        Bee maya = new Bee("John D.", "j@d.fr", 40.730610, -73.935242, "::1", Role.BEE);
        given(this.beeService.byId(anyString())).willReturn(Optional.of(maya));

        this.mvc.perform(get("/api/bee/foobar"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(content().json(gson.toJson(maya)));
    }

    @Test
    public void shouldNotBeAbleToRetrieveNonexistentBees() throws Exception {
        given(this.beeService.byId(anyString())).willReturn(empty());

        this.mvc.perform(get("/api/bee/foobar"))
            .andExpect(status().isNotFound());
    }

    @Test
    public void shouldBeAbleToCreateANewBee() throws Exception {
        Bee maya = new Bee("John D.", "j@d.fr", 40.730610, -73.935242, "::1", Role.BEE);
        given(this.beeService.add(maya)).willReturn(maya);

        Field id = maya.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(maya, UUID.randomUUID().toString());

        this.mvc.perform(post("/api/bee")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(maya)))
            .andExpect(status().isCreated())
            .andExpect(header().string("Location",
                Matchers.allOf(
                    Matchers.startsWith("/api/bee/"),
                    Matchers.endsWith(maya.getId())
                )
            ));
    }
}
