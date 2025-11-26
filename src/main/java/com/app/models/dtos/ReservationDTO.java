package com.app.models.dtos;

import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ReservationDTO {
    private Long id;
    private LocalDate reservationDate;
    private LocalDate dueDate;
    private String status;
    private MemberDTO member;
    private BookDTO book;
}
