package com.azo.backend.gadapp.gad_back.services;

import java.util.List;
import java.util.Optional;
import com.azo.backend.gadapp.gad_back.models.entities.User;

//3. Tercero Create UserService implementación del crud

public interface UserService {

  //lista data
  List<User> findAll();

  //buscar data por id
  Optional<User> findById(Long id);

  //guardar data
  User save(User user);

  //eliminar data
  void remove(Long id);
}
