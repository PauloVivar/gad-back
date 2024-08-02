package com.azo.backend.gadapp.gad_back.models.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

public class PasswordResetRequestDto {

  @NotEmpty(message = "El email es requerido.")
  @Email(message = "Ingrese un email válido.")
  @Column(nullable = false, unique = true)
  private String email;

  // Constructor
  public PasswordResetRequestDto() {
  }

  public PasswordResetRequestDto(String email) {
    this.email = email;
  }

  // Getters y Setters
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

}
