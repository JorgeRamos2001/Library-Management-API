package com.app.models.entities;

import com.app.models.enums.LibrarianRoles;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "librarians")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Librarian {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "librarian_id")
    private Long id;
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;
    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;
    @Column(name = "dui", nullable = false, length = 10)
    private String dui;
    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;
    @Column(name = "address", nullable = false, length = 1000)
    private String address;
    @Column(name = "email", nullable = false, length = 100)
    private String email;
    @Column(name = "password", nullable = false, length = 10)
    private String password;
    @Column(name = "hired_on", nullable = false)
    private LocalDate hiredOn;
    @Enumerated(EnumType.STRING)
    @Column(name = "librarian_role", nullable = false, length = 50)
    private LibrarianRoles librarianRole;
    @Column(name = "is_active", nullable = false)
    private boolean isActive;
}
