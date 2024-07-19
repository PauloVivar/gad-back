package com.azo.backend.gadapp.gad_back.models.dto;

public class UserRegistrationDTO {

  private String username;
  private String password;
  private String email;
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
