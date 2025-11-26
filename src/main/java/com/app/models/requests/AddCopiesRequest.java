package com.app.models.requests;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AddCopiesRequest {
    @NotNull( message = "Copies cannot be null")
    @Min( value = 1, message = "Copies must be greater than 0")
    private int copies;
    @NotNull( message = "Book ID cannot be null")
    private Long bookId;
}
