package com.app.models.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class UpdateBookAuthorRequest {
    @NotNull( message = "Book ID cannot be null")
    private Long bookId;
    @NotNull( message = "Authors cannot be null")
    @NotEmpty( message = "At least one author must be selected")
    private List<Long> authorIds;
}
