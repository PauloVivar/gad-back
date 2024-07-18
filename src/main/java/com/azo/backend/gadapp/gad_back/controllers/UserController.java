package com.azo.backend.gadapp.gad_back.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.azo.backend.gadapp.gad_back.models.dto.UserDto;
import com.azo.backend.gadapp.gad_back.models.entities.User;
import com.azo.backend.gadapp.gad_back.models.request.UserRequest;
import com.azo.backend.gadapp.gad_back.services.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.PutMapping;


//5. Quinto Create Controller -> Mapeo de endpoints, finalización del CRUD

@RestController
@RequestMapping("/api/v1/users")
//@CrossOrigin(origins = "http://localhost:5173/")  //cors
@CrossOrigin(originPatterns = "*")                  //cors solo pruebas
public class UserController {

  @Autowired
  private UserService service;

  //get
  @GetMapping
  public List<UserDto> list(){
    //solo pruebas
    // try {
    //   Thread.sleep(2000l);
    // } catch (InterruptedException e) {
    //   e.printStackTrace();
    // }
    return service.findAll();
  }

  //método custom para paginación
  @GetMapping("/page/{page}")
  public Page<UserDto> list(@PathVariable Integer page){
    Pageable pageable = PageRequest.of(page, 5);
    return service.findAll(pageable);
  }

  //***Forma alternativa para el get
  // @GetMapping("/{id}")
  // public User show (@PathVariable Long id){
  //   return service.findById(id).orElseThrow();
  // }

  //getById -> orElseThrow()
  @GetMapping("/{id}")
  public ResponseEntity<?> show (@PathVariable Long id){
    Optional<UserDto> userOptional = service.findById(id);
    if(userOptional.isPresent()){
      return ResponseEntity.ok(userOptional.orElseThrow());
    }
    return ResponseEntity.notFound().build();
  }

  //***Forma alternativa para el post
  // @PostMapping
  // @ResponseStatus(HttpStatus.CREATED)
  // public User create (@RequestBody User user){
  //   return service.save(user);
  // }

  //post
  @PostMapping
  public ResponseEntity<?> create (@Valid @RequestBody User user, BindingResult result){
    if(result.hasErrors()){
      return validation(result);
    }
    
    // Verificar si el username ya existe en la base de datos
    if (service.existsByUsername(user.getUsername())) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Campo ya existe");
    }

    // Guardar el usuario si no existe
    UserDto userDb = service.save(user);
    return ResponseEntity.status(HttpStatus.CREATED).body(userDb);
  }

  // //post unique
  // @PostMapping("/register")
  // public ResponseEntity<?> registerUser (@Valid @RequestBody User user){
  // // Verificar si el username ya existe en la base de datos
  // if (service.existsByUsername(user.getUsername())) {
  //   return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Username already exists");
  // }
  // // Guardar el usuario si no existe
  // User userDb = service.save(user);
  // return ResponseEntity.status(HttpStatus.CREATED).body(userDb);
  // }

  //update
  @PutMapping("/{id}")
  public ResponseEntity<?> update (@Valid @RequestBody UserRequest user, BindingResult result, @PathVariable Long id){
    if(result.hasErrors()){
      return validation(result);
    }
    Optional<UserDto> userOptional = service.update(user, id);
    if(userOptional.isPresent()){
      return ResponseEntity.status(HttpStatus.CREATED).body(userOptional.orElseThrow());
    }
    return ResponseEntity.notFound().build();
  }

  //delete
  @DeleteMapping("/{id}")
  public ResponseEntity<?> remove (@PathVariable Long id){
    Optional<UserDto> useOptional = service.findById(id);
    if(useOptional.isPresent()){
      service.remove(id);
      return ResponseEntity.noContent().build(); //204
    }
    return ResponseEntity.notFound().build();    //404
  }

  //metodo para validar entrada de data
  private ResponseEntity<?> validation(BindingResult result) {
    Map<String, String> errors = new HashMap<>();
    result.getFieldErrors().forEach(err -> {
      //errors.put(err.getField(), "El campo" + err.getField() + " " + err.getDefaultMessage());
      errors.put(err.getField(), err.getDefaultMessage());
    });
    return ResponseEntity.badRequest().body(errors);
  }

}