package com.app.models.dtos;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AuthorDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String biography;
    private String nationality;
}
