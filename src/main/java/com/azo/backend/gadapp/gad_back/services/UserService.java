package com.azo.backend.gadapp.gad_back.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.azo.backend.gadapp.gad_back.models.dto.UserDto;
import com.azo.backend.gadapp.gad_back.models.dto.UserRegistrationDTO;
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

  //*test guardar data registration
  UserDto saveRegistration(UserRegistrationDTO userRegistration, String ipAddress);

  //actualizar data
  Optional<UserDto> update (UserRequest user, Long id);
  //Optional<UserDto> update (UserRegistrationDTO user, Long id);

  //eliminar data
  void remove(Long id);

  //validar campos unique
  boolean existsByUsername(String username);
  boolean existsByEmail(String email);

  //reset password
  //método para encontrar usuario por email
  Optional<User> findByEmail(String email);

  //métodos para el restablecimiento de contraseña
  String createPasswordResetCodeForUser(User user);
  String validatePasswordResetCode(String code);
  User getUserByPasswordResetCode(String code);
  void changeUserPassword(User user, String newPassword);
  
}
