package com.azo.backend.gadapp.gad_back.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azo.backend.gadapp.gad_back.models.entities.User;
import com.azo.backend.gadapp.gad_back.services.UserService;

//5. Quinto create Controller

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

  @Autowired
  private UserService service;

  @GetMapping
  public List<User> list(){
    return service.findAll();
  }

  //***Forma alternativa para el get
  // @GetMapping("/{id}")
  // public User show (@PathVariable Long id){
  //   return service.findById(id).orElseThrow();
  // }

  //get -> orElseThrow()
  @GetMapping("/{id}")
  public ResponseEntity<?> show (@PathVariable Long id){
    Optional<User> userOptional = service.findById(id);
    if(userOptional.isPresent()){
      return ResponseEntity.ok(userOptional.orElseThrow());
    }
    return ResponseEntity.notFound().build();
  }

  //post
  @PostMapping
  public ResponseEntity<?> create (@RequestBody User user){
    User userDb = service.save(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(userDb);
  }

  //update
  public ResponseEntity<?> update (@RequestBody User user, @PathVariable Long id){
    Optional<User> userOptional = service.findById(id);
    if(userOptional.isPresent()){
      User userDb = userOptional.orElseThrow();
      userDb.setUsername(user.getUsername());
      userDb.setEmail(user.getEmail());

      return ResponseEntity.status(HttpStatus.CREATED).body(userDb);
    }
    return ResponseEntity.notFound().build();
  }
}