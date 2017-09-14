package com.cloudbees.hive.http;

import com.cloudbees.hive.model.Bee;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * @author Adrien Lecharpentier
 */
@RunWith(SpringRunner.class)
@WebMvcTest(controllers = {BeeAPI.class})
public class BeeAPITest {

    @Autowired private MockMvc mvc;
    @MockBean private BeeService beeService;

    @Autowired private Gson gson;
    @Autowired private ObjectMapper objectMapper;

    private Authentication auth(Bee bee) {
        return new UsernamePasswordAuthenticationToken(bee, null, Collections.emptyList());
    }

    @Test
    public void shouldNotKnowWhoAmIIfUnauthenticated() throws Exception {
        this.mvc
            .perform(get("/api/me"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    public void shouldBeAbleToSayWhoAmI() throws Exception {
        Bee maya = new Bee("John D.", "j@d.fr", 40.730610, -73.935242);
        Field id = maya.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(maya, UUID.randomUUID().toString());

        given(this.beeService.add(maya)).willReturn(maya);

        this.mvc
            .perform(
                get("/api/me")
                    .with(user("j@d.fr"))
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(content().json(gson.toJson(maya)))
            .andDo(print())
        ;
    }

    @Test
    public void shouldNotBeAbleToAccessTheHiveIfUnauthenticated() throws Exception {
        this.mvc
            .perform(get("/api/hive"))
            .andExpect(status().isUnauthorized())
        ;
    }

    @Test
    @WithMockUser
    public void shouldBeAbleToGetTheHiveMembers() throws Exception {
        List<Bee> hive = Arrays.asList(
            new Bee("Adrien L.", "a@l.fr", 48.864716, 2.349014),
            new Bee("Carlos R.", "a@h.fr", 37.392529, -5.994072)
        );
        Field id = Bee.class.getDeclaredField("id");
        id.setAccessible(true);
        id.set(hive.get(0), UUID.randomUUID().toString());
        id.set(hive.get(1), UUID.randomUUID().toString());

        given(this.beeService.all()).willReturn(hive);

        this.mvc
            .perform(get("/api/hive"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", Matchers.hasSize(hive.size())))
            .andExpect(jsonPath("$[*].id", Matchers.hasItem(Matchers.any(String.class))))
            .andExpect(jsonPath("$[*].name", Matchers.hasItems("Adrien L.", "Carlos R.")))
            .andExpect(jsonPath("$[*].latitude", Matchers.hasItems(48.864716, 37.392529)))
            .andExpect(jsonPath("$[*].longitude", Matchers.hasItems(2.349014, -5.994072)))
        ;
    }

    @Test
    public void shouldNotBeAbleToUpdateAnotherBee() throws Exception {
        Bee maya = new Bee("John D.", "j@d.fr", 40.730610, -73.935242);
        Field id = maya.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(maya, UUID.randomUUID().toString());

        given(this.beeService.add(maya)).willReturn(maya);

        this.mvc
            .perform(
                post("/api/bee")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(maya))
                    .with(csrf().asHeader())
            )
            .andExpect(status().isForbidden())
        ;
    }

    @Test
    public void shouldBeAbleToUpdateCurrentBee() throws Exception {
        Bee maya = new Bee("John D.", "j@d.fr", 40.730610, -73.935242);
        Field id = maya.getClass().getDeclaredField("id");
        id.setAccessible(true);
        id.set(maya, UUID.randomUUID().toString());

        given(this.beeService.add(maya)).willReturn(maya);

        this.mvc
            .perform(
                post("/api/bee")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(maya))
                    .with(csrf().asHeader())
            )
            .andExpect(status().isOk())
            .andExpect(content().json(gson.toJson(maya)))
        ;
    }
}
