package com.azo.backend.gadapp.gad_back.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

//4. Cuarto Implementación de EmailService -> volver realidad el CRUD

@Service
public class EmailServiceImpl implements EmailService {

  @Autowired
  private JavaMailSender mailSender;

  @Override
  public void sendPasswordResetEmail(String to, String code) {
      SimpleMailMessage message = new SimpleMailMessage();
      message.setTo(to);
      message.setSubject("Restablecimiento de contraseña");
      message.setText("Para restablecer tu contraseña, utiliza el siguiente token: " + code +
                      "\nIngresa este token en la página de restablecimiento de contraseña.");
      mailSender.send(message);
  }
}
