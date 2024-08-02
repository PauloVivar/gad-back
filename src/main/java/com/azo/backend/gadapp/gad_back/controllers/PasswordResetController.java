package com.azo.backend.gadapp.gad_back.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.azo.backend.gadapp.gad_back.models.dto.PasswordResetDto;
import com.azo.backend.gadapp.gad_back.models.dto.PasswordResetRequestDto;
import com.azo.backend.gadapp.gad_back.models.entities.User;
import com.azo.backend.gadapp.gad_back.services.UserService;

import jakarta.validation.Valid;

//5. Quinto Create Controller -> Mapeo de endpoints, finalización del CRUD

@RestController
@RequestMapping("/api/v1/password")
//@CrossOrigin(origins = "http://localhost:5173/")  //cors
@CrossOrigin(originPatterns = "*")                  //cors solo pruebas
public class PasswordResetController {
  
  @Autowired
  private UserService userService;

  @PostMapping("/reset-request")
  public ResponseEntity<?> resetPasswordRequest(@Valid @RequestBody PasswordResetRequestDto requestDto) {
    return userService.findByEmail(requestDto.getEmail())
        .map(user -> {
            @SuppressWarnings("unused")
            String code = userService.createPasswordResetCodeForUser(user);
            // Aquí no devolvemos el código por razones de seguridad
            return ResponseEntity.ok("Se ha enviado un código de verificación a su correo electrónico.");
        })
        .orElse(ResponseEntity.badRequest().body("Usuario no encontrado"));
  }

  @PostMapping("/reset")
  public ResponseEntity<?> resetPassword(@Valid @RequestBody PasswordResetDto resetDto) {
    String result = userService.validatePasswordResetCode(resetDto.getCode());
    if(result != null) {
        return ResponseEntity.badRequest().body("Código inválido o expirado");
    }
    User user = userService.getUserByPasswordResetCode(resetDto.getCode());
    if(user != null) {
        userService.changeUserPassword(user, resetDto.getNewPassword());
        return ResponseEntity.ok("Contraseña cambiada exitosamente");
    } else {
        return ResponseEntity.badRequest().body("Error al cambiar la contraseña");
    }
  }
}