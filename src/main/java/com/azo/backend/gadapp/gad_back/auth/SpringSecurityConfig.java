package com.azo.backend.gadapp.gad_back.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.azo.backend.gadapp.gad_back.auth.filters.JwtAuthenticationFilter;
import com.azo.backend.gadapp.gad_back.auth.filters.JwtValidationFilter;


// 1. Primero -> Configuración de Spring Security para Login

@Configuration
public class SpringSecurityConfig {

  @Autowired
  private AuthenticationConfiguration authenticationConfiguration;

  @Bean                                                                   //Componente de spring
  PasswordEncoder passwordEncoder(){
    //return NoOpPasswordEncoder.getInstance();                           //Solo para pruebas
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationManager authenticationManager() throws Exception {        //Cuando se manda con BCryptPassword
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http
        //.authorizeHttpRequests()
        .authorizeHttpRequests(authRules -> authRules
            .requestMatchers(HttpMethod.GET, "/api/v1/users").permitAll()
            //.requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
            .anyRequest().authenticated())
            //.and()
        .addFilter(new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager()))  //para login
        .addFilter(new JwtValidationFilter(authenticationConfiguration.getAuthenticationManager()))
        .csrf(config -> config.disable())               //desabilitar cuanto es API-REST, Monolito viene por defecto
        .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    return http.build();
  }
}
