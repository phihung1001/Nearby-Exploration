package com.example.foodtourbackend.DTO;

public class UpdatePasswordRequest {
  private String oldPassword;
  private String newPassword;
  private String confirmNewPassword;

  public UpdatePasswordRequest(String oldPassword, String newPassword, String confirmNewPassword) {
    this.oldPassword = oldPassword;
    this.newPassword = newPassword;
    this.confirmNewPassword = confirmNewPassword;
  }
  public String getOldPassword() {
    return oldPassword;
  }
  public void setOldPassword(String oldPassword) {
    this.oldPassword = oldPassword;
  }
  public String getNewPassword() {
    return newPassword;
  }
  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }
  public String getConfirmNewPassword() {
    return confirmNewPassword;
  }
  public void setConfirmNewPassword(String confirmPassword) {
    this.confirmNewPassword = confirmNewPassword;
  }

}
