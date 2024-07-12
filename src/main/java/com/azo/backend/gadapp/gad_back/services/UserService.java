package com.azo.backend.gadapp.gad_back.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.azo.backend.gadapp.gad_back.models.dto.UserDto;
import com.azo.backend.gadapp.gad_back.models.entities.User;
import com.azo.backend.gadapp.gad_back.models.request.UserRequest;

//3. Tercero Create UserService -> Implementación del CRUD
//Interacción con la tabla user_custom(dto) y cliente

public interface UserService {

  //listar data
  List<UserDto> findAll();

  //método custom para listar todo con paginación data users
  Page<UserDto> findAll(Pageable pageable);

  //buscar data por id
  Optional<UserDto> findById(Long id);

  //guardar data
  UserDto save(User user);

  //actualizar data
  Optional<UserDto> update (UserRequest user, Long id);

  //eliminar data
  void remove(Long id);

  //validar campos unique
  boolean existsByUsername(String username);
  boolean existsByEmail(String email);
  
}
