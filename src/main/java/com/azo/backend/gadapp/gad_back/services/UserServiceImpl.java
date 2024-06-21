package com.azo.backend.gadapp.gad_back.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.azo.backend.gadapp.gad_back.models.entities.User;
import com.azo.backend.gadapp.gad_back.repositories.UserRepository;

//4. Cuarto Implementación de UserService antes creado(crud)

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository repository;

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

  //Transactional ya no es solo de lectura ya que save guarda y actualiza
  @Override
  @Transactional
  public User save(User user) {
    return repository.save(user);
  }

  @Override
  public void remove(Long id) {
    repository.deleteById(id);
  }

}
