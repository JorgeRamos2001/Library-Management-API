package com.app.services;

import com.app.models.dtos.LoanDTO;
import com.app.models.requests.CreateLoanRequest;

import java.time.LocalDate;
import java.util.List;

public interface ILoanService {
    LoanDTO save(CreateLoanRequest loan);
    LoanDTO returnBook(Long loanId, String returnCondition);
    LoanDTO findById(Long id);
    List<LoanDTO> findAll();
    List<LoanDTO> findAllByMemberIdAndStatus(Long memberId, String status);
    List<LoanDTO> findAllByMemberId(Long memberId);
    List<LoanDTO> findOverdueLoans();
    List<LoanDTO> findAllByLibrarianId(Long librarianId);
    List<LoanDTO> findAllByBookId(Long bookId);
    List<LoanDTO> findAllByBookCopyId(Long bookCopyId);
    List<LoanDTO> findByLoanDateBetween(LocalDate startDate, LocalDate endDate);
    LoanDTO updateStatus(Long id, String status);
    void markLoansAsOverdue();
}
