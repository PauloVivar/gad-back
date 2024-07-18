package com.azo.backend.gadapp.gad_back.auth.filters;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.azo.backend.gadapp.gad_back.models.entities.User;
import com.azo.backend.gadapp.gad_back.repositories.UserRepository;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.azo.backend.gadapp.gad_back.auth.TokenJwtConfig.*;

//2. Segundo -> Generación de token cuando User se Logea

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final UserRepository userRepository;
  private final AuthenticationManager authenticationManager;

  //private AuthenticationManager authenticationManager;

  public JwtAuthenticationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
    this.authenticationManager = authenticationManager;
    this.userRepository = userRepository;
    setAuthenticationManager(authenticationManager);
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException {

    User user = null;
    String username = null;
    String password = null;

    try {
      user = new ObjectMapper().readValue(request.getInputStream(), User.class);
      username = user.getUsername();
      password = user.getPassword();
      // logger.info("Username desde request InputStream (raw)" + username);
      // logger.info("Password desde request InputStream (raw)" + password);

    } catch (StreamReadException e) {
      e.printStackTrace();
    } catch (DatabindException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }

    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);
    return authenticationManager.authenticate(authToken);
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
      Authentication authResult) throws IOException, ServletException {

        //solo pruebas
        // try {
        //   Thread.sleep(2000l);
        // } catch (InterruptedException e) {
        //   e.printStackTrace();
        // }
    
    String username = ((org.springframework.security.core.userdetails.User) authResult.getPrincipal())
        .getUsername();

    //obtener id de user
    Long userId = getUserIdFromUsername(username);  // Este método necesita ser implementado

    // String originalInput = SECRET_KEY + "." + username;
    // String token = Base64.getEncoder().encodeToString(originalInput.getBytes());
    Collection<? extends GrantedAuthority> roles = authResult.getAuthorities();
    boolean isAdmin = roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

    ClaimsBuilder claims = Jwts.claims();
    // claims.put("authorities", new ObjectMapper().writeValueAsString(roles));
    // claims.put("isAdmin", isAdmin);   
    claims.add("authorities", new ObjectMapper().writeValueAsString(roles));  //se pasa el objeto roles como json (se utliza ObjectMapper para eso)
    claims.add("isAdmin", isAdmin);                                           //validar si el role es Admin
    claims.add("username", username);
    claims.add("userId", userId);                                             //test
    
    String token = Jwts.builder()
        //.claims(claims)
        .claims((Map<String, ?>) claims.build())                                  //roles
        .subject(username)
        .signWith(SECRET_KEY)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + 3600000))
        .compact();

    response.addHeader(HEADER_AUTHORIZATION, PREFIX_TOKEN + token);

    Map<String, Object> body = new HashMap<>();
    body.put("token", token);
    body.put("message", String.format("Hola %s, has iniciado sesion satisfactoriamente", username));
    body.put("username", username);
    body.put("userId", userId);                  // Incluimos el ID del usuario en la respuesta
    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    response.setStatus(200);
    response.setContentType("application/json");

  }

  private Long getUserIdFromUsername(String username) {
    return userRepository.findIdByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
  }

  @Override
  protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException failed) throws IOException, ServletException {
    Map<String, Object> body = new HashMap<>();
    body.put("message", "Authentication error, username o password incorrectos!");
    body.put("error", failed.getMessage());

    response.getWriter().write(new ObjectMapper().writeValueAsString(body));
    response.setStatus(401);
    response.setContentType("application/json");

  }
}
