package com.app.repositories;

import com.app.models.entities.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface IMemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByDui(String dui);
    Optional<Member> findByEmail(String email);
    List<Member> findAllByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(String firstName, String lastName);
    boolean existsByDui(String dui);
    boolean existsByEmail(String email);
}
