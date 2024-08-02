package com.azo.backend.gadapp.gad_back.repositories;

import java.util.Optional;
import org.springframework.data.repository.CrudRepository;

import com.azo.backend.gadapp.gad_back.models.entities.PasswordResetCode;
import com.azo.backend.gadapp.gad_back.models.entities.User;

//2. Segundo Create Repositorio -> donde estan los métodos CRUD de SPRING BOOT
//CrudRepository ORM(JPA con hibernet) para realizar el CRUD, interacción directa con la DB

public interface PasswordResetCodeRepository extends CrudRepository<PasswordResetCode, Long> {

  //método custom para findByCode
  Optional<PasswordResetCode> findByCode(String code);
  void deleteByUser(User user);

}
