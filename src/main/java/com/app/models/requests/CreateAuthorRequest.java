package com.app.models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateAuthorRequest {
    @NotBlank(message = "First name cannot be empty or null")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;
    @NotBlank(message = "Last name cannot be empty or null")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;
    @NotBlank(message = "Biography cannot be empty or null")
    private String biography;
    @NotBlank(message = "Nationality cannot be empty or null")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String nationality;
}
