package com.azo.backend.gadapp.gad_back.services;

import java.util.List;
import java.util.Optional;

import com.azo.backend.gadapp.gad_back.models.dto.UserDto;
import com.azo.backend.gadapp.gad_back.models.entities.User;
import com.azo.backend.gadapp.gad_back.models.request.UserRequest;

//3. Tercero Create UserService -> Implementación del CRUD

public interface UserService {

  //lista data
  List<UserDto> findAll();

  //buscar data por id
  Optional<UserDto> findById(Long id);

  //guardar data
  UserDto save(User user);

  //actualizar data
  Optional<UserDto> update (UserRequest user, Long id);

  //eliminar data
  void remove(Long id);

  //test unique
  boolean existsByUsername(String username);
  boolean existsByEmail(String email);
  
}
