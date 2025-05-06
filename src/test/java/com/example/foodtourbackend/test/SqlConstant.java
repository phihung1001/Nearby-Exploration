package com.example.foodtourbackend.test;

public class SqlConstant {

  public static final String RESET_CUSTOMER_DATA = """
      DELETE FROM customer;
    """;
  public static final String INSERT_CUSTOMER = """
        INSERT INTO customer (
            id, age, full_name, phone_number, email, address, role, password, gender
        ) VALUES (
            1, 25, 'Nguyễn Văn A', '0123456789', 'test@example.com', 'Hà Nội', 'CUSTOMER', 'hashed_password', 'MALE'
        );
        """;
  public static final String INSERT_CUSTOMER2 = """
        INSERT INTO customer (
            id, age, full_name, phone_number, email, address, role, password, gender
        ) VALUES (
            1, 25, 'Nguyễn Văn A', '0123456789', 'test@example.com', 'Hà Nội', 'CUSTOMER', 'hashed_password', 'MALE'
        );
        INSERT INTO customer (
            id, age, full_name, phone_number, email, address, role, password, gender
        ) VALUES (
            2, 20, 'Nguyễn Văn B', '012345678', 'test1@example.com', 'Hà Nội', 'CUSTOMER', 'hashed_password', 'MALE'
        );
        """;
}
