package com.example.lazada_game.web.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

    @NotBlank(message = "Full Name is required")
    private String fullname;
    @NotBlank(message = "Username is required")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
    @NotBlank(message = "Tel is required")
    private String tel;

    @NotBlank(message = "Address is required")
    private String address;
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be valid")
    private String email;
}
