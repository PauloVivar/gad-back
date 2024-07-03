package com.azo.backend.gadapp.gad_back.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azo.backend.gadapp.gad_back.models.IUser;
import com.azo.backend.gadapp.gad_back.models.dto.UserDto;
import com.azo.backend.gadapp.gad_back.models.dto.mapper.DtoMapperUser;
import com.azo.backend.gadapp.gad_back.models.entities.Role;
import com.azo.backend.gadapp.gad_back.models.entities.User;
import com.azo.backend.gadapp.gad_back.models.request.UserRequest;
import com.azo.backend.gadapp.gad_back.repositories.RoleRepository;
import com.azo.backend.gadapp.gad_back.repositories.UserRepository;

//4. Cuarto Implementación de UserService -> volver realidad el CRUD

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository repository;

  @Autowired
  private RoleRepository repositoryRole;

  @Autowired
  private PasswordEncoder passwordEncoder;

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
  public void remove(Long id) {
    repository.deleteById(id);
  }

  @Override
  public boolean existsByUsername(String username) {
    return repository.toString().equals(username);
  }

  @Override
  public boolean existsByEmail(String email) {
    return repository.toString().equals(email);
  }

  //logica para asignar o eliminar un usuario como role admin
  private List<Role> getRoles(IUser user){
    Optional<Role> ou = repositoryRole.findByName("ROLE_USER");
    List<Role> roles = new ArrayList<>();
    if(ou.isPresent()){
      roles.add(ou.orElseThrow());
    }

    if(user.isAdmin()){
      Optional<Role> oa = repositoryRole.findByName("ROLE_ADMIN");
      if(oa.isPresent()){
        roles.add(oa.orElseThrow());
      }
    }
    return roles;
  }

}
