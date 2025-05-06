package com.example.foodtourbackend.service;

import com.example.foodtourbackend.entity.Customer;
import com.example.foodtourbackend.repository.CustomerRepository;
import com.example.foodtourbackend.service.serviceImpl.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

public class UserDetailsServiceTest {
  @Mock
  private CustomerRepository customerRepository;

  @InjectMocks
  private UserDetailsServiceImpl userDetailsService;

  private Customer customer;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    // Tạo đối tượng Customer giả lập
    customer = new Customer();
    customer.setId(1L);
    customer.setEmail("test@example.com");
    customer.setPassword("testPassword");
    customer.setRole("ROLE_USER");
  }

  @Test
  void testLoadUserByUsername_Success() {
    // Arrange
    when(customerRepository.findByEmail("test@example.com")).thenReturn(Optional.of(customer));

    // Act
    UserDetails userDetails = userDetailsService.loadUserByUsername("test@example.com");

    // Assert
    assertNotNull(userDetails, "UserDetails should not be null");
    assertEquals("test@example.com", userDetails.getUsername(), "Username should be test@example.com");
    assertEquals("testPassword", userDetails.getPassword(), "Password should be testPassword");
    assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")),
      "Authorities should contain ROLE_USER");
  }

  @Test
  void testLoadUserByUsername_UserNotFound() {
    // Arrange
    when(customerRepository.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

    // Act & Assert
    UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class,
      () -> userDetailsService.loadUserByUsername("nonexistent@example.com"),
      "Expected UsernameNotFoundException to be thrown");
    assertEquals("Người dùng không tồn tại với email: nonexistent@example.com", exception.getMessage(),
      "Exception message should match expected");
  }
}
