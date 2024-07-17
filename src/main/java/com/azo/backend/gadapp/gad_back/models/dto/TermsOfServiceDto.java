package com.azo.backend.gadapp.gad_back.models.dto;

import java.time.LocalDate;

public class TermsOfServiceDto {

  private Long id;
  private String version;
  private String content;
  private LocalDate effectiveDate;

  public TermsOfServiceDto() {
  }

  public TermsOfServiceDto(Long id, String version, String content, LocalDate effectiveDate) {
    this.id = id;
    this.version = version;
    this.content = content;
    this.effectiveDate = effectiveDate;
  }

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
