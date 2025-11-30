package com.app.models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateGenreRequest {
    @NotBlank(message = "Name cannot be empty or null")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;
}
