package com.azo.backend.gadapp.gad_back.auth;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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
        .requestMatchers(HttpMethod.GET, "/api/v1/users", "/api/v1/users/page/{page}", "/api/v1/terms").permitAll()
        .requestMatchers(HttpMethod.GET, "/api/v1/users/{id}").hasAnyRole("ADMIN", "USER")
        .requestMatchers(HttpMethod.POST, "/api/v1/users").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/v1/terms", "/api/v1/terms/record").hasRole("ADMIN")
        .requestMatchers("/api/v1/users/*").hasRole("ADMIN")
        .requestMatchers("/api/v1/terms/*").hasRole("ADMIN")
        //.requestMatchers(HttpMethod.DELETE, "/api/v1/users/{id}").hasRole("ADMIN")
        //.requestMatchers(HttpMethod.PUT, "/api/v1/users/{id}").hasRole("ADMIN")
        .anyRequest().authenticated())
        //.and()
      .addFilter(new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager()))      //para 
      .addFilter(new JwtValidationFilter(authenticationConfiguration.getAuthenticationManager()))
      .csrf(config -> config.disable())                                                                    //desabilitar cuanto es API-REST, Monolito viene por defecto
      .sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .cors(cors -> cors.configurationSource(corsConfigurationSource()));
    return http.build();
  }

  //Configuracion de Cors en Spring Security
  @Bean
  CorsConfigurationSource corsConfigurationSource(){
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
    config.setAllowedOriginPatterns(Arrays.asList("*"));                      //cors solo pruebas
    config.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
    config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
    config.setAllowCredentials(true);

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", config);
    return source;
  }

  //Configuración de Filtro para darle prioridad a Cors
  @Bean
  FilterRegistrationBean<CorsFilter> corsFilter(){
    FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>(
      new CorsFilter(corsConfigurationSource()));
    bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
    return bean;
  }
  
}
