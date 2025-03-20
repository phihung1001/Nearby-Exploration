package com.example.foodtourbackend.DTO;

public class RegisterRequest {
    private final String fullName;
    private final String email;
    private final String password;
    private final String confirmPassword;
    private final String phoneNumber;
    private final String address;
    private final String gender;

    public RegisterRequest(String fullName, String email, String password,
                           String confirmPassword, String phoneNumber,
                           String address, String gender) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.gender = gender;
    }

    // Getter methods (nên thêm để có thể lấy dữ liệu từ DTO)
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getConfirmPassword() { return confirmPassword; }
    public String getPhoneNumber() { return phoneNumber; }
    public String getAddress() { return address; }
    public String getGender() { return gender; }
}
