package com.app.models.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class CreateLoanRequest {
    @NotNull( message = "Member ID cannot be null")
    private Long memberId;
    @NotNull( message = "Book ID cannot null")
    private Long bookCopyId;
    @NotNull( message = "Librarian ID cannot be null")
    private Long librarianId;
}
