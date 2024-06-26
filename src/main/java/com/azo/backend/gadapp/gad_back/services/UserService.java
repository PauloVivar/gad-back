package com.azo.backend.gadapp.gad_back.services;

import java.util.List;
import java.util.Optional;
import com.azo.backend.gadapp.gad_back.models.entities.User;
import com.azo.backend.gadapp.gad_back.models.request.UserRequest;

//3. Tercero Create UserService -> Implementación del CRUD

public interface UserService {

  //lista data
  List<User> findAll();

  //buscar data por id
  Optional<User> findById(Long id);

  //guardar data
  User save(User user);

  //actualizar data
  Optional<User> update (UserRequest user, Long id);

  //eliminar data
  void remove(Long id);

  //test unique
  boolean existsByUsername(String username);
  boolean existsByEmail(String email);
  
}
