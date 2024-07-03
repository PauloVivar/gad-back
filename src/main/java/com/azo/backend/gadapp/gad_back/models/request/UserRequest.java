package com.azo.backend.gadapp.gad_back.models.request;

import com.azo.backend.gadapp.gad_back.models.IUser;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

//1.1 Create Clases UserRequest personalización de JSON

public class UserRequest implements IUser {

  @NotBlank(message = "El username es requerido.")
  @Column(unique = true)
  private String username;

  @NotEmpty(message = "El email es requerido.")
  @Email(message = "Ingrese un email válido.")
  @Column(unique = true)
  private String email;

  //role admin
  private boolean admin;

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public boolean isAdmin() {
    return admin;
  }

  public void setAdmin(boolean admin) {
    this.admin = admin;
  }

}
