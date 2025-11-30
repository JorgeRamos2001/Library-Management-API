package com.app.repositories;

import com.app.models.dtos.LoanDTO;
import com.app.models.entities.*;
import com.app.models.enums.LoanStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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
    List<Loan> findAllByLoanDateBetween(LocalDate startDate, LocalDate endDate);
    boolean existsByBookCopyAndStatus(BookCopy bookCopy, LoanStatus status);
    @Query("SELECT COUNT(l) > 0 FROM Loan l WHERE l.member.id = :memberId AND l.bookCopy.book.id = :bookId AND l.status = :status")
    boolean existsByMemberAndBookAndStatus(
            @Param("memberId") Long memberId,
            @Param("bookId") Long bookId,
            @Param("status") LoanStatus status
    );
    int countByMemberAndStatus(Member member, LoanStatus status);

    List<Loan> findAllByBookCopy_Book(Book book);
    List<Loan> findAllByStatusAndDueDateBefore(LoanStatus status, LocalDate dueDate);
}
