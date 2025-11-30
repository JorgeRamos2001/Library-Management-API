package com.app.models.dtos;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class MemberDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String dui;
    private String email;
    private LocalDateTime createdOn;
    private boolean isActive;
}
