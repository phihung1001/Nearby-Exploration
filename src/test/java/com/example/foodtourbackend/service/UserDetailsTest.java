package com.example.foodtourbackend.service;

import com.example.foodtourbackend.service.serviceImpl.UserDetailsImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

public class UserDetailsTest {
  private UserDetailsImpl userDetails;

  @BeforeEach
  void setUp() {
    // Tạo đối tượng UserDetailsImpl với dữ liệu giả lập
    userDetails = new UserDetailsImpl(
      1L, // userId
      "testUser", // username
      "testPassword", // password
      Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER")) // authorities
    );
  }

  @Test
  void testGetUserId() {
    // Kiểm tra phương thức getUserId
    assertEquals(1L, userDetails.getUserId(), "User ID should be 1");
  }

  @Test
  void testGetUsername() {
    // Kiểm tra phương thức getUsername
    assertEquals("testUser", userDetails.getUsername(), "Username should be testUser");
  }

  @Test
  void testGetPassword() {
    // Kiểm tra phương thức getPassword
    assertEquals("testPassword", userDetails.getPassword(), "Password should be testPassword");
  }

  @Test
  void testGetAuthorities() {
    // Kiểm tra phương thức getAuthorities
    assertNotNull(userDetails.getAuthorities(), "Authorities should not be null");
    assertTrue(userDetails.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")), "Authorities should contain ROLE_USER");
  }

  @Test
  void testIsAccountNonExpired() {
    // Kiểm tra phương thức isAccountNonExpired
    assertTrue(userDetails.isAccountNonExpired(), "Account should not be expired");
  }

  @Test
  void testIsAccountNonLocked() {
    // Kiểm tra phương thức isAccountNonLocked
    assertTrue(userDetails.isAccountNonLocked(), "Account should not be locked");
  }

  @Test
  void testIsCredentialsNonExpired() {
    // Kiểm tra phương thức isCredentialsNonExpired
    assertTrue(userDetails.isCredentialsNonExpired(), "Credentials should not be expired");
  }

  @Test
  void testIsEnabled() {
    // Kiểm tra phương thức isEnabled
    assertTrue(userDetails.isEnabled(), "Account should be enabled");
  }
}
