package com.azo.backend.gadapp.gad_back.repositories;

import org.springframework.data.repository.CrudRepository;
import com.azo.backend.gadapp.gad_back.models.entities.User;

//2. Segundo Create Repositorio -> donde estan los métodos CRUD de SPRING BOOT
//CrudRepository ORM(JPA con hibernet) para realizar el CRUD

public interface UserRepository extends CrudRepository<User, Long> {

}
