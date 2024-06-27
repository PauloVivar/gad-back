package com.azo.backend.gadapp.gad_back.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azo.backend.gadapp.gad_back.models.entities.User;
import com.azo.backend.gadapp.gad_back.models.request.UserRequest;
import com.azo.backend.gadapp.gad_back.repositories.UserRepository;

//4. Cuarto Implementación de UserService -> volver realidad el CRUD

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository repository;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  @Transactional(readOnly = true)
  public List<User> findAll() {
    return (List<User>) repository.findAll();
  }

  @Override
  @Transactional(readOnly = true)
  public Optional<User> findById(Long id) {
    return repository.findById(id);
  }

  //Transactional ya no es readOnly ya que save guarda y actualiza
  @Override
  @Transactional
  public User save(User user) {
    String passwordBCrypt = passwordEncoder.encode(user.getPassword());
    user.setPassword(passwordBCrypt);
    return repository.save(user);
  }

  //Se utiliza UserRequest ya que no se pasa el password
  @Override
  @Transactional
  public Optional<User> update(UserRequest user, Long id) {
    Optional<User> o = this.findById(id);
    User userOptional = null;
    if(o.isPresent()){
      User userDb = o.orElseThrow();
      userDb.setUsername(user.getUsername());
      userDb.setEmail(user.getEmail());
      userOptional = this.save(userDb);
    }
    return Optional.ofNullable(userOptional);
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

}
