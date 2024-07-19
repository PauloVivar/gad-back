package com.azo.backend.gadapp.gad_back.models.dto;

public class TermsInteractionDTO {

  private Long userId;
  private boolean accepted;
  private String ipAddress;


  //getters and setters
  public Long getUserId() {
    return userId;
  }
  public void setUserId(Long userId) {
    this.userId = userId;
  }
  public boolean isAccepted() {
    return accepted;
  }
  public void setAccepted(boolean accepted) {
    this.accepted = accepted;
  }
  public String getIpAddress() {
    return ipAddress;
  }
  public void setIpAddress(String ipAddress) {
    this.ipAddress = ipAddress;
  }

}
