package com.app.models.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateLibrarianRequest {
    @NotBlank(message = "First name cannot be empty or null")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    @NotBlank(message = "Last name cannot be empty or null")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    @NotBlank(message = "DUI cannot be empty or null")
    @Size(min = 9, max = 9, message = "DUI can only have 9 characters")
    private String dui;
    @NotBlank(message = "Phone number cannot be empty or null")
    @Size(min = 8, max = 8, message = "Phone number can only have 8 characters")
    private String phoneNumber;
    @NotBlank(message = "Address cannot be empty or null")
    private String address;
    @NotBlank(message = "Email cannot be empty or null")
    @Email(message = "Invalid email format")
    private String email;
    @NotBlank(message = "Password cannot be empty or null")
    @Size(min = 6, max = 10, message = "Password must be between 6 and 10 characters")
    private String password;
}
