package com.azo.backend.gadapp.gad_back.repositories;

import org.springframework.data.repository.CrudRepository;
import com.azo.backend.gadapp.gad_back.models.entities.User;

//2. Segundo create Repositorio dond estan los métodos crud

//CrudRepository ORM(JPA con hivernet) para realizar el crud
public interface UserRepository extends CrudRepository<User, Long> {

}
