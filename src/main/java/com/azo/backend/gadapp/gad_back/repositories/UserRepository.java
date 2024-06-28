package com.azo.backend.gadapp.gad_back.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import com.azo.backend.gadapp.gad_back.models.entities.User;

//2. Segundo Create Repositorio -> donde estan los métodos CRUD de SPRING BOOT
//CrudRepository ORM(JPA con hibernet) para realizar el CRUD

public interface UserRepository extends CrudRepository<User, Long> {

  //método custom 2 formas
  
  //1ra forma -> siguiendo la nomenclatura de Spring boot
  Optional<User> findByUsername(String username);

  //2da forma -> consulta personalizanda (nombre del método) -> "and u.email=?2" -> en caso de 2do parametro
  @Query("select u from User u where u.username=?1")
  Optional<User> getUserByUsername(String username);

}
