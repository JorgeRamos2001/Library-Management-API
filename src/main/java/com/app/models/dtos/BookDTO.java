package com.app.models.dtos;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BookDTO {
    private Long id;
    private String isbn;
    private String title;
    private String description;
    private LocalDate publicationDate;
    private String genre;
    private Long bookCopies;
    private List<AuthorDTO> authors;
}
