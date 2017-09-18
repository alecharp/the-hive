package com.cloudbees.hive.config;

import com.cloudbees.hive.service.BeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
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
    @Value("${the-hive.domain}") private String domain;

    @Autowired
    public Security(BeeService beeService) {
        this.beeService = beeService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
            .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .and()
                .logout()
                .logoutUrl("/api/me/logout")
                .deleteCookies("JSESSIONID", "XSRF-TOKEN")
                .logoutSuccessHandler((req, resp, ex) -> resp.sendRedirect("/"))
            .and()
                .csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        ;
        // @formatter:on
    }

    @Bean
    public PrincipalExtractor principalExtractor() {
        return map -> {
            String email = (String) map.get(EMAIL_ATTR);
            if (email == null || email.isEmpty() || !email.endsWith(this.domain)) {
                throw new AccountRejectedException("Cannot accept connection from " + email);
            }
            return beeService.authenticate(
                email,
                (String) map.get(NAME_ATTR)
            );
        };
    }

    @Bean @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}
