package com.azo.backend.gadapp.gad_back.models.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "terms_of_service")
//crear fechas de creación y  mod automaticas
@EntityListeners(AuditingEntityListener.class)
public class TermsOfService {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false)
  private String version;

  @Column(nullable = false, columnDefinition = "TEXT")
  private String content;

  @Column(nullable = false)
  private LocalDate effectiveDate;

  @CreatedDate
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdDate;

  @LastModifiedDate
  @Column(nullable = false)
  private LocalDateTime lastModifiedDate;

  //Getters and Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public LocalDate getEffectiveDate() {
    return effectiveDate;
  }

  public void setEffectiveDate(LocalDate effectiveDate) {
    this.effectiveDate = effectiveDate;
  }

}
