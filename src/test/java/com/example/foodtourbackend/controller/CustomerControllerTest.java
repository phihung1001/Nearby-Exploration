package com.example.foodtourbackend.controller;

import com.example.foodtourbackend.DTO.request.CommentDTO;
import com.example.foodtourbackend.DTO.request.CustomerRequestDTO;
import com.example.foodtourbackend.DTO.request.UpdatePasswordRequestDTO;
import com.example.foodtourbackend.DTO.response.ApiResponse;
import com.example.foodtourbackend.DTO.response.CommentResponseDTO;
import com.example.foodtourbackend.DTO.response.CustomerResponseDTO;
import com.example.foodtourbackend.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class CustomerControllerTest {

  @Mock
  private CustomerService customerService;

  @InjectMocks
  private CustomerController customerController;

  private MockMvc mockMvc;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    mockMvc = MockMvcBuilders.standaloneSetup(customerController).build();
  }

  @Test
  void testGetById() throws Exception {
    // Arrange
    Long customerId = 1L;
    CustomerResponseDTO customerResponseDTO = new CustomerResponseDTO();
    when(customerService.getById(customerId)).thenReturn(customerResponseDTO);

    // Act & Assert
    mockMvc.perform(get("/customer/{id}", customerId))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.id").exists()); // Adjust this based on actual response structure
  }

  @Test
  void testUpdate() throws Exception {
    // Arrange
    Long customerId = 1L;
    CustomerRequestDTO customerRequestDTO = new CustomerRequestDTO();
    CustomerResponseDTO updatedCustomer = new CustomerResponseDTO();
    when(customerService.update(customerId, customerRequestDTO)).thenReturn(updatedCustomer);

    // Act & Assert
    mockMvc.perform(post("/customer/update/{id}", customerId)
        .contentType("application/json")
        .content("{\"name\":\"New Name\"}")) // Use actual request body here
      .andExpect(status().isOk());
  }

  @Test
  void testUpdatePassword() throws Exception {
    // Arrange
    Long customerId = 1L;
    UpdatePasswordRequestDTO request = new UpdatePasswordRequestDTO();
    when(customerService.updatePassword(customerId, request)).thenReturn(Map.of("status", "success"));

    // Act & Assert
    mockMvc.perform(post("/customer/update/password/{id}", customerId)
        .contentType("application/json")
        .content("{\"oldPassword\":\"old\", \"newPassword\":\"new\"}")) // Use actual request body here
      .andExpect(status().isOk());
  }

  @Test
  void testSearchByImage() throws Exception {
    // Arrange
    MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", new byte[]{});
    when(customerService.searchByImage(file)).thenReturn("Search result");

    // Act & Assert
    mockMvc.perform(multipart("/customer/searchByImage")
        .file(file))
      .andExpect(status().isOk())
      .andExpect(content().string("Search result"));
  }

  @Test
  void testUpgrade() throws Exception {
    // Arrange
    when(customerService.upgrade()).thenReturn(Map.of("status", "success"));

    // Act & Assert
    mockMvc.perform(post("/customer/upgrade-provider"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.status").value("success"));
  }

  @Test
  void testComment_Success() throws Exception {
    // Arrange
    Long restaurantId = 1L;
    CommentDTO commentDTO = new CommentDTO();
    commentDTO.setComment("Great place!");
    CommentResponseDTO commentResponseDTO = new CommentResponseDTO();
    ResponseEntity<ApiResponse<CommentResponseDTO>> responseEntity = new ResponseEntity<>(
      new ApiResponse<>("thanh cong", commentResponseDTO), HttpStatus.OK
    );
    when(customerService.comment(restaurantId, commentDTO)).thenReturn(responseEntity);

    // Act & Assert
    mockMvc.perform(post("/customer/comment/{id}", restaurantId)
        .contentType("application/json")
        .content("{\"content\":\"Great place!\"}"))
      .andExpect(status().isOk())
      .andExpect(jsonPath("$.data").exists()); // Adjust this based on actual response structure
  }

  @Test
  void testComment_BadRequest() throws Exception {
    // Arrange
    Long restaurantId = 1L;
    CommentDTO commentDTO = new CommentDTO();
    commentDTO.setComment(""); // Invalid content

    // Act & Assert
    mockMvc.perform(post("/customer/comment/{id}", restaurantId)
        .contentType("application/json")
        .content("{\"content\":\"\"}"))
      .andExpect(status().isBadRequest())
      .andExpect(content().string("Nội dung bình luận không hợp lệ"));
  }

  @Test
  void testComment_InternalServerError() throws Exception {
    // Arrange
    Long restaurantId = 1L;
    CommentDTO commentDTO = new CommentDTO();
    commentDTO.setComment("Great place!");
    when(customerService.comment(restaurantId, commentDTO)).thenThrow(new RuntimeException("Error"));

    // Act & Assert
    mockMvc.perform(post("/customer/comment/{id}", restaurantId)
        .contentType("application/json")
        .content("{\"content\":\"Great place!\"}"))
      .andExpect(status().isInternalServerError())
      .andExpect(content().string("Đã xảy ra lỗi khi xử lý yêu cầu"));
  }

}
