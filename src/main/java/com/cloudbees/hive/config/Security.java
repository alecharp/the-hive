package com.cloudbees.hive.config;

import com.cloudbees.hive.model.Bee;
import com.cloudbees.hive.model.Role;
import com.cloudbees.hive.service.BeeService;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Configuration
public class Security {
    @EnableWebSecurity
    @EnableGlobalMethodSecurity(prePostEnabled = true)
    public static class OAuth2SecurityConfig extends WebSecurityConfigurerAdapter {
        private final BeeService beeService;

        public OAuth2SecurityConfig(BeeService beeService) {
            this.beeService = beeService;
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            //@formatter:off
            http
              .authorizeRequests()
                .anyRequest().hasRole("BEE")
                .and()
              .logout()
                .logoutUrl("/api/me/logout")
                .and()
              .oauth2Login()
                .authorizationEndpoint()
                  .baseUri("/login/oauth2")
                  .and()
                .userInfoEndpoint()
                  .userService(this.userService())
                  .and()
                .and()
            ;
            //@formatter:on
        }

        private OAuth2UserService<OAuth2UserRequest, OAuth2User> userService() {
            return userRequest -> {
                String userInfoURI = userRequest.getClientRegistration().getProviderDetails().getUserInfoEndpoint().getUri();
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + userRequest.getAccessToken().getTokenValue());
                HttpEntity entity = new HttpEntity<>("", headers);
                ResponseEntity<Map> response = restTemplate.exchange(userInfoURI, HttpMethod.GET, entity, Map.class);
                Map<String, String> userAttributes = (Map<String, String>) response.getBody();
                if (userAttributes == null) {
                    return null;
                }

                String email = userAttributes.get("email");
                return beeService.byEmail(email)
                      .orElseGet(() ->
                            beeService.add(new Bee(userAttributes.get("name"), email, email.endsWith("@cloudbees.com") ? Role.BEE : Role.ANONYMOUS))
                      );
            };
        }

        @Override
        public void configure(WebSecurity web) {
            //@formatter:off
            web
              .ignoring()
                .antMatchers("/", "/index.html", "/js/main.js", "favicon.ico")
            ;
            //@formatter:on
        }
    }
}
