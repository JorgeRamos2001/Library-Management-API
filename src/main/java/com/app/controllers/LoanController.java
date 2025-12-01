package com.app.controllers;

import com.app.models.dtos.LoanDTO;
import com.app.models.requests.CreateLoanRequest;
import com.app.services.ILoanService;
import com.app.services.implementations.LoanServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {
    private final ILoanService loanService;

    @PostMapping
    public ResponseEntity<LoanDTO> save(@Valid @RequestBody CreateLoanRequest loan){
        return new ResponseEntity<>(loanService.save(loan), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LoanDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(loanService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<LoanDTO>> findAll(){
        return ResponseEntity.ok(loanService.findAll());
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<LoanDTO>> findAllByMemberId(
            @PathVariable Long memberId,
            @RequestParam(required = false) String status
    ){
        if (status != null) {
            return ResponseEntity.ok(loanService.findAllByMemberIdAndStatus(memberId, status));
        }
        return ResponseEntity.ok(loanService.findAllByMemberId(memberId));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<LoanDTO>> findAllByBookId(@PathVariable Long bookId){
        return ResponseEntity.ok(loanService.findAllByBookId(bookId));
    }

    @GetMapping("/librarian/{librarianId}")
    public ResponseEntity<List<LoanDTO>> findAllByLibrarianId(@PathVariable Long librarianId){
        return ResponseEntity.ok(loanService.findAllByLibrarianId(librarianId));
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<LoanDTO>> findAllOverdueLoans(){
        return ResponseEntity.ok(loanService.findOverdueLoans());
    }

    @GetMapping("/bookCopy/{bookCopyId}")
    public ResponseEntity<List<LoanDTO>> findAllByBookCopyId(@PathVariable Long bookCopyId){
        return ResponseEntity.ok(loanService.findAllByBookCopyId(bookCopyId));
    }

    @GetMapping("/range")
    public ResponseEntity<List<LoanDTO>> findByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return ResponseEntity.ok(loanService.findByLoanDateBetween(startDate, endDate));
    }

    @PostMapping("/{id}/return")
    public ResponseEntity<LoanDTO> returnBook(@PathVariable Long id, @RequestParam String condition) {
        return ResponseEntity.ok(loanService.returnBook(id, condition));
    }

    @PutMapping("/{loanId}")
    public ResponseEntity<LoanDTO> updateStatus(@PathVariable Long loanId, @RequestParam String status){
        return ResponseEntity.ok(loanService.updateStatus(loanId, status));
    }
}
