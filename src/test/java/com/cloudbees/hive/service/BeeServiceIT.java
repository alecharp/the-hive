package com.cloudbees.hive.service;

import com.cloudbees.hive.model.Bee;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection.H2;

/**
 * @author Adrien Lecharpentier
 */
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(connection = H2)
@Import(BeeService.class)
public class BeeServiceIT {
    @Autowired private BeeService beeService;

    @Test
    public void shouldBeAbleToSaveBeesInsideTheHive() {
        List<Bee> expectedHive = Arrays.asList(
            new Bee("Adrien L.", "a@l.fr", 48.864716, 2.349014, "::1"),
            new Bee("Carlos R.", "a@h.fr", 37.392529, -5.994072, "::1")
        );
        expectedHive.forEach(beeService::add);
        assertThat(this.beeService.all()).containsExactlyElementsOf(expectedHive);
    }
}
