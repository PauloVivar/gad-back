package com.azo.backend.gadapp.gad_back.models.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class PasswordResetDto {

  @NotEmpty(message = "El código no puede estar vacío")
  @Size(min = 6, max = 6, message = "El código debe tener 6 dígitos")
  private String code;

  @NotBlank(message = "El password es requerido.")
  @Column(nullable = false)
  @Size(min = 5, message = "La contraseña debe tener al menos 5 caracteres")
  private String newPassword;

  // Constructor
  public PasswordResetDto() {
  }

  public PasswordResetDto(String code, String newPassword) {
    this.code = code;
    this.newPassword = newPassword;
  }

  // Getters y Setters
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

}
