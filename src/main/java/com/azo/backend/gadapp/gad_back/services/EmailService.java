package com.azo.backend.gadapp.gad_back.services;

//3. Tercero Create EmailService -> Implementación del CRUD

public interface EmailService {

  //sendPasswordResetEmail
  void sendPasswordResetEmail(String to, String code);
  
}
