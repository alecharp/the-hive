package com.cloudbees.hive.config;

import com.cloudbees.hive.service.BeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * @author Adrien Lecharpentier
 */
@Configuration
@EnableOAuth2Sso
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class Security extends WebSecurityConfigurerAdapter {
    private static final String EMAIL_ATTR = "email";
    private static final String NAME_ATTR = "name";

    private final BeeService beeService;

    @Autowired
    public Security(BeeService beeService) {
        this.beeService = beeService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .formLogin()
                .successForwardUrl("/")
            .and()
                .logout()
                .logoutUrl("/api/me/logout")
                .deleteCookies("JSESSIONID")
            .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        ;
        // @formatter:on
    }

    @Bean
    public PrincipalExtractor principalExtractor() {
        return map ->
            beeService.authenticate(
                (String)map.get(EMAIL_ATTR),
                (String)map.get(NAME_ATTR)
            );
    }
}
