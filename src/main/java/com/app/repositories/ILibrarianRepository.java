package com.app.repositories;

import com.app.models.entities.Librarian;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ILibrarianRepository extends JpaRepository<Librarian, Long> {
    Optional<Librarian> findByDui(String dui);
    Optional<Librarian> findByEmail(String email);
    List<Librarian> findAllByIsActive(boolean active);
    List<Librarian> findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    boolean existsByDui(String dui);
    boolean existsByEmail(String email);
}
