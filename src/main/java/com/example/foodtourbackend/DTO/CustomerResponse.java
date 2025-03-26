package com.example.foodtourbackend.DTO;

public class CustomerResponse {
  private int id;
  private int age;
  private String fullName;
  private String phoneNumber;
  private String email;
  private String address;
  private String gender;

  public CustomerResponse() {}
  public CustomerResponse(int id, int age, String fullName, String phoneNumber, String email, String address, String gender) {
    this.id = id;
    this.age = age;
    this.fullName = fullName;
    this.phoneNumber = phoneNumber;
    this.email = email;
    this.address = address;
    this.gender = gender;
  }

  public long getId() {
    return id;
  }
  public void setId(int id) {
    this.id = id;
  }
  public int getAge() {
    return age;
  }
  public void setAge(int age) {
    this.age = age;
  }
  public String getFullName() {
    return fullName;
  }
  public void setFullName(String fullName) {
    this.fullName = fullName;
  }
  public String getPhoneNumber() {
    return phoneNumber;
  }
  public void setPhoneNumber(String phoneNumber) {
    this.phoneNumber = phoneNumber;
  }
  public String getEmail() {
    return email;
  }
  public void setEmail(String email) {
    this.email = email;
  }
  public String getAddress() {
    return address;
  }
  public void setAddress(String address) {
    this.address = address;
  }
  public String getGender() {
    return gender;
  }
  public void setGender(String gender) {
    this.gender = gender;
  }
}
