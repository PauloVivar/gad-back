package com.azo.backend.gadapp.gad_back.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig {

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(authRules -> authRules
        .requestMatchers(HttpMethod.GET, "/api/v1/users").permitAll()
        .anyRequest().authenticated())
        //.and()
        .csrf(config -> config.disable())      //desabilitar cuanto es APIREST, Monolito viene por defecto
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
      return http.build();
  }
}
