package com.app.models.requests;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateBookRequest {
    @NotBlank( message = "ISBN cannot be empty or null")
    @Size(min = 13, max = 20, message = "ISBN must be between 13 and 20 characters")
    private String isbn;
    @NotBlank( message = "Title cannot be empty or null")
    @Size(min = 2, max = 100, message = "Title must be between 2 and 100 characters")
    private String title;
    @NotBlank( message = "Description cannot be empty or null")
    @Size(min = 20, max = 1000, message = "Description must be between 20 and 1000 characters")
    private String description;
    @NotNull( message = "Publication date cannot be null")
    @PastOrPresent( message = "Publication date must be in the past")
    private LocalDate publicationDate;
    @NotNull( message = "Book copies cannot be null")
    @Min( value = 1, message = "Book copies must be greater than 0")
    private Long bookCopies;
    @NotNull( message = "Genre cannot be null")
    private Long genreId;
    @NotNull( message = "Authors cannot be null")
    @NotEmpty( message = "At least one author must be selected")
    private List<Long> authorIds;
}
