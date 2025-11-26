package com.app.models.dtos;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LibrarianDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String dui;
    private String phoneNumber;
    private String address;
    private String email;
    private LocalDate hiredOn;
    private String librarianRole;
    private boolean isActive;
}
