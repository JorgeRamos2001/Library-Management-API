package com.app.repositories;

import com.app.models.entities.*;
import com.app.models.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ILoanRepository extends JpaRepository<Loan, Long> {
    List<Loan> findAllByMember(Member member);
    List<Loan> findAllByMemberAndStatus(Member member, LoanStatus status);
    Optional<Loan> findByBookCopyAndMember(BookCopy bookCopy, Member member);
    List<Loan> findAllByBookCopy(BookCopy bookCopy);
    List<Loan> findAllByLibrarian(Librarian librarian);
    List<Loan> findAllByStatus(LoanStatus status);
}
