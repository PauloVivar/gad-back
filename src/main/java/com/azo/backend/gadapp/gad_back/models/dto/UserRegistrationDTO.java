package com.azo.backend.gadapp.gad_back.models.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UserRegistrationDTO {

  @NotBlank(message = "El username es requerido.")
  @Column(nullable = false, unique = true)
  private String username;

  @NotBlank(message = "El password es requerido.")
  @Column(nullable = false)
  private String password;

  @NotEmpty(message = "El email es requerido.")
  @Email(message = "Ingrese un email válido.")
  @Column(nullable = false, unique = true)
  private String email;

  @NotNull(message = "La aceptación de los términos de servicio es requerida.")
  private Boolean acceptedTerms;

  // Constructor
  public UserRegistrationDTO() {
  }

  public UserRegistrationDTO(String username, String password, String email,  boolean acceptedTerms) {
    this.username = username;
    this.password = password;
    this.email = email;
    this.acceptedTerms = acceptedTerms;
  }

  // Getters y Setters
  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }
  
  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public Boolean getAcceptedTerms() {
    return acceptedTerms;
  }

  public void setAcceptedTerms(Boolean acceptedTerms) {
    this.acceptedTerms = acceptedTerms;
  }

}
