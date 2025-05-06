package com.example.foodtourbackend.test;

public class JsonConstant {
  public static final String CUSTOMER_REQUEST = """
        {
            "age": 25,
            "fullName": "Nguyễn Văn A",
            "phoneNumber": "0123456789",
            "email": "test@example.com",
            "address": "Hà Nội",
            "role": "CUSTOMER",
            "password": "123456",
            "gender": "MALE"
        }
        """;

  public static final String CUSTOMER_RESPONSE = """
        {
            "id": 1,
            "age": 25,
            "fullName": "Nguyễn Văn A",
            "phoneNumber": "0123456789",
            "email": "test@example.com",
            "address": "Hà Nội",
            "role": "CUSTOMER",
            "gender": "MALE"
        }
        """;
  public static final String CUSTOMER_REQUEST_UPDATE = """
        {
            "age": 26,
            "fullName": "Nguyễn Văn A",
            "phoneNumber": "0999999999",
            "email": "testupdate@example.com",
            "address": "Cầu Giấy, Hà Nội",
            "password": "123456",
            "gender": "MALE"
        }
        """;
  public static final String CUSTOMER_REQUEST_UPDATE_DUP_EMAIL = """
        {
            "age": 26,
            "fullName": "Nguyễn Văn A",
            "phoneNumber": "0999999999",
            "email": "test1@example.com",
            "address": "Cầu Giấy, Hà Nội",
            "password": "123456",
            "gender": "MALE"
        }
        """;
  public static final String CUSTOMER_REQUEST_UPDATE_DUP_PHONE = """
        {
            "age": 26,
            "fullName": "Nguyễn Văn A",
            "phoneNumber": "012345678",
            "email": "testupdate@example.com",
            "address": "Cầu Giấy, Hà Nội",
            "password": "123456",
            "gender": "MALE"
        }
        """;
  public static final String CUSTOMER_REQUEST_UPDATE_PASSWORD = """
        {
            "oldPassword": "hashed_password",
            "newPassword": "new_password",
            "confirmNewPassword": "new_password"
        }
        """;
}
