package com.azo.backend.gadapp.gad_back.models.entities;

import java.util.List;

import com.azo.backend.gadapp.gad_back.models.IUser;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

//1. Primero Crear Entidad -> Entity User

@Entity
@Table(name="users")
public class User implements IUser {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

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

  //Relación muchos a muchos, utilización de tabla ternaria
  @ManyToMany
  @JoinTable(
    name = "users_roles",
    joinColumns = @JoinColumn(name="user_id"),
    inverseJoinColumns = @JoinColumn(name="role_id"),
    uniqueConstraints = { @UniqueConstraint(columnNames = {"user_id", "role_id"}) }
  )
  private List<Role> roles;

  //Transient para no mapear este campo en la DB
  @Transient
  private boolean admin;
  
  //Getters and Setters
  public Long getId() {
    return id;
  }
  public void setId(Long id) {
    this.id = id;
  }
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
  public List<Role> getRoles() {
    return roles;
  }
  public void setRoles(List<Role> roles) {
    this.roles = roles;
  }

  //Como se implementa IUser (Interfaz User) indicamos que isAdmin estamos sobre escribiendo
  @Override
  public boolean isAdmin() {
    return admin;
  }
  public void setAdmin(boolean admin) {
    this.admin = admin;
  }

}
