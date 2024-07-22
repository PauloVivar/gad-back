package com.azo.backend.gadapp.gad_back.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azo.backend.gadapp.gad_back.models.IUser;
import com.azo.backend.gadapp.gad_back.models.dto.UserDto;
import com.azo.backend.gadapp.gad_back.models.dto.UserRegistrationDTO;
import com.azo.backend.gadapp.gad_back.models.dto.mapper.DtoMapperUser;
import com.azo.backend.gadapp.gad_back.models.entities.Role;
import com.azo.backend.gadapp.gad_back.models.entities.User;
import com.azo.backend.gadapp.gad_back.models.request.UserRequest;
import com.azo.backend.gadapp.gad_back.repositories.RoleRepository;
import com.azo.backend.gadapp.gad_back.repositories.TermsAcceptanceRepository;
import com.azo.backend.gadapp.gad_back.repositories.UserRepository;

//4. Cuarto Implementación de UserService -> volver realidad el CRUD

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository repository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private TermsAcceptanceRepository termsAcceptanceRepository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  //test
  @Autowired
  private TermsService termsService;

  @Override
  @Transactional(readOnly = true)
  public List<UserDto> findAll() {
    List<User> users = (List<User>) repository.findAll();
    return users
      .stream()
      .map( u -> DtoMapperUser.builder().setUser(u).build())
      .collect(Collectors.toList());

    //return (List<User>) repository.findAll();
  }

  //método custom para paginación
  @Override
  @Transactional(readOnly = true)
  public Page<UserDto> findAll(Pageable pageable) {
    Page<User> usersPage = repository.findAll(pageable);
    return usersPage.map(u -> DtoMapperUser.builder().setUser(u).build());
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<UserDto> findById(Long id) {
    return repository.findById(id).map(u -> DtoMapperUser
      .builder()
      .setUser(u)
      .build());
    
    //return repository.findById(id);
  }

  //Transactional ya no es readOnly ya que save guarda y actualiza
  @Override
  @Transactional
  public UserDto save(User user) {
    String passwordBCrypt = passwordEncoder.encode(user.getPassword());
    user.setPassword(passwordBCrypt);
    //get roles
    List<Role> roles = getRoles(user);
    user.setRoles(roles);
    return DtoMapperUser.builder().setUser(repository.save(user)).build();
    //return repository.save(user);
  }

  //test
  @Override
  @Transactional
  public UserDto saveRegistration(UserRegistrationDTO userRegistration, String ipAddress) {
    if (existsByUsername(userRegistration.getUsername())) {
          throw new RuntimeException("El nombre de usuario ya existe");
      }
      if (existsByEmail(userRegistration.getEmail())) {
          throw new RuntimeException("El email ya está registrado");
      }

      User newUser = new User();
      newUser.setUsername(userRegistration.getUsername());
      newUser.setPassword(passwordEncoder.encode(userRegistration.getPassword()));
      newUser.setEmail(userRegistration.getEmail());

      // Asignar rol de usuario por defecto
      List<Role> roles = new ArrayList<>();
      roleRepository.findByName("ROLE_USER").ifPresent(roles::add);
      newUser.setRoles(roles);

      User savedUser = repository.save(newUser);

      // Registrar la aceptación de términos
      termsService.recordTermsInteraction(savedUser.getId(), true, ipAddress);

      return DtoMapperUser.builder().setUser(savedUser).build();
  }

  //Se utiliza UserRequest ya que no se pasa el password
  @Override
  @Transactional
  public Optional<UserDto> update(UserRequest user, Long id) {
    Optional<User> o = repository.findById(id);
    User userOptional = null;
    if(o.isPresent()){

      //get roles
      List<Role> roles = getRoles(user);

      User userDb = o.orElseThrow();
      userDb.setRoles(roles);
      userDb.setUsername(user.getUsername());
      userDb.setEmail(user.getEmail());
      userOptional = repository.save(userDb);
    }
    return Optional.ofNullable(DtoMapperUser.builder().setUser(userOptional).build());
    //return Optional.ofNullable(userOptional);
  }
  
  @Override
  @Transactional
  public void remove(Long id) {
    // Primero, eliminamos todas las aceptaciones de términos asociadas
    termsAcceptanceRepository.deleteByUserId(id);
    // Luego, eliminamos el usuario
    repository.deleteById(id);
  }

  //validar campos unique
  @Override
  public boolean existsByUsername(String username) {
    return repository.toString().equals(username);
  }

  //validar campos unique
  @Override
  public boolean existsByEmail(String email) {
    return repository.toString().equals(email);
  }

  //logica para asignar o eliminar un usuario como role admin
  private List<Role> getRoles(IUser user){
    Optional<Role> ou = roleRepository.findByName("ROLE_USER");
    List<Role> roles = new ArrayList<>();
    if(ou.isPresent()){
      roles.add(ou.orElseThrow());
    }

    if(user.isAdmin()){
      Optional<Role> oa = roleRepository.findByName("ROLE_ADMIN");
      if(oa.isPresent()){
        roles.add(oa.orElseThrow());
      }
    }
    return roles;
  }

}
