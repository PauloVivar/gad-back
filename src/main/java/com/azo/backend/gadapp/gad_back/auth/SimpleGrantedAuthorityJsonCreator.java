package com.azo.backend.gadapp.gad_back.auth;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

//Clase abstracta
public abstract class SimpleGrantedAuthorityJsonCreator {

  @JsonCreator
  public SimpleGrantedAuthorityJsonCreator(@JsonProperty("authority") String role) {
  }
  
}
