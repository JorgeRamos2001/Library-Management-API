package com.app.services.implementations;

import com.app.exceptions.BadRequestException;
import com.app.exceptions.OperationNotPermittedException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.models.dtos.*;
import com.app.models.entities.*;
import com.app.models.enums.BookCondition;
import com.app.models.enums.LoanStatus;
import com.app.models.requests.CreateLoanRequest;
import com.app.repositories.*;
import com.app.services.ILoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LoanServiceImpl implements ILoanService {

    private final ILoanRepository loanRepository;
    private final IMemberRepository memberRepository;
    private final IBookCopyRepository bookCopyRepository;
    private final ILibrarianRepository librarianRepository;
    private final IBookRepository bookRepository;

    @Override
    public LoanDTO save(CreateLoanRequest loan) {
        Member member = memberRepository.findById(loan.getMemberId()).orElseThrow(() -> new ResourceNotFoundException("Member with id: " + loan.getMemberId() + " not found."));

        Librarian librarian = librarianRepository.findById(loan.getLibrarianId()).orElseThrow(() -> new ResourceNotFoundException("Librarian with id: " + loan.getLibrarianId() + " not found."));
        BookCopy bookCopy = bookCopyRepository.findById(loan.getBookCopyId()).orElseThrow(() -> new ResourceNotFoundException("Book copy with id: " + loan.getBookCopyId() + " not found."));
        if(loanRepository.existsByBookCopyAndStatus(bookCopy, LoanStatus.LOANED)) throw new OperationNotPermittedException("Book copy with id: " + loan.getBookCopyId() + " already has a loan.");

        if(loanRepository.existsByMemberAndBookAndStatus(member.getId(), bookCopy.getBook().getId(), LoanStatus.LOANED)) throw new OperationNotPermittedException("Member with id: " + loan.getMemberId() + " already has a loan for book with id: " + loan.getBookCopyId());

        if(loanRepository.countByMemberAndStatus(member, LoanStatus.LOANED) >= 3) throw new OperationNotPermittedException("Member with id: " + loan.getMemberId() + " has reached the maximum number of loans.");

        if(loanRepository.countByMemberAndStatus(member, LoanStatus.OVERDUE) >= 1) throw new OperationNotPermittedException("Member with id: " + loan.getMemberId() + " has overdue loans.");


        return mapToDTO(loanRepository.save(Loan
                .builder()
                .loanDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(7))
                .status(LoanStatus.LOANED)
                .bookCopy(bookCopy)
                .member(member)
                .librarian(librarian)
                .build()));
    }

    @Override
    public LoanDTO returnBook(Long loanId, String returnCondition) {
        Loan loan = loanRepository.findById(loanId).orElseThrow(() -> new ResourceNotFoundException("Loan with id: " + loanId + " not found."));
        BookCopy bookCopy = loan.getBookCopy();

        try {
            BookCondition newCondition = BookCondition.valueOf(returnCondition.toUpperCase());
            bookCopy.setCondition(newCondition);
            bookCopyRepository.save(bookCopy);
            loan.setReturnDate(LocalDate.now());
            loan.setStatus(LoanStatus.RETURNED);
            return mapToDTO(loanRepository.save(loan));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Estado inválido: " + returnCondition + ". Valores permitidos: NEW, USED, DAMAGED, LOST");
        }
    }

    @Override
    public LoanDTO findById(Long id) {
        return mapToDTO(loanRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Loan with id: " + id + " not found.")));
    }

    @Override
    public List<LoanDTO> findAll() {
        return loanRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<LoanDTO> findAllByMemberIdAndStatus(Long memberId, String status) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("Member with id: " + memberId + " not found."));
        try {
            LoanStatus loanStatus = LoanStatus.valueOf(status.toUpperCase());
            return loanRepository.findAllByMemberAndStatus(member, loanStatus).stream()
                    .map(this::mapToDTO)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Estado inválido: " + status + ". Valores permitidos: NEW, USED, DAMAGED, LOST");
        }
    }

    @Override
    public List<LoanDTO> findAllByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("Member with id: " + memberId + " not found."));

        return loanRepository.findAllByMember(member).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<LoanDTO> findOverdueLoans() {
        return loanRepository.findAllByStatus(LoanStatus.OVERDUE).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<LoanDTO> findAllByLibrarianId(Long librarianId) {
        Librarian librarian = librarianRepository.findById(librarianId).orElseThrow(() -> new ResourceNotFoundException("Librarian with id: " + librarianId + " not found."));
        return loanRepository.findAllByLibrarian(librarian).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<LoanDTO> findAllByBookId(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book with id: " + bookId + " not found."));
        return loanRepository.findAllByBookCopy_Book(book).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<LoanDTO> findAllByBookCopyId(Long bookCopyId) {
        BookCopy bookCopy = bookCopyRepository.findById(bookCopyId).orElseThrow(() -> new ResourceNotFoundException("Book copy with id: " + bookCopyId + " not found."));
        return loanRepository.findAllByBookCopy(bookCopy).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<LoanDTO> findByLoanDateBetween(LocalDate startDate, LocalDate endDate) {
        return loanRepository.findAllByLoanDateBetween(startDate, endDate).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public LoanDTO updateStatus(Long id, String status) {
        Loan loan = loanRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Loan with id: " + id + " not found."));

        try {
            LoanStatus loanStatus = LoanStatus.valueOf(status.toUpperCase());
            loan.setStatus(loanStatus);
            return mapToDTO(loanRepository.save(loan));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status: " + status + ". values : LOANED, RETURNED, OVERDUE");
        }
    }

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    public void markLoansAsOverdue() {
        List<Loan> loans = loanRepository.findAllByStatusAndDueDateBefore(LoanStatus.LOANED, LocalDate.now());

        loans.forEach(loan -> {
            loan.setStatus(LoanStatus.OVERDUE);
        });

        loanRepository.saveAll(loans);
    }

    private LoanDTO mapToDTO(Loan loan) {
        return LoanDTO
                .builder()
                .id(loan.getId())
                .loanDate(loan.getLoanDate())
                .dueDate(loan.getDueDate())
                .returnDate(loan.getReturnDate())
                .status(loan.getStatus().name())
                .bookCopyId(loan.getBookCopy().getId())
                .book(mapToDTO(loan.getBookCopy().getBook(), loan.getBookCopy().getBook().getAuthors(), loan.getBookCopy().getBook().getGenre(), (long) loan.getBookCopy().getBook().getBookCopies().size()))
                .member(mapToDTO(loan.getMember()))
                .librarian(mapToDTO(loan.getLibrarian()))
                .build();
    }

    private BookDTO mapToDTO(Book book, List<Author> authors, Genre genre, Long bookCopies) {
        return BookDTO
                .builder()
                .id(book.getId())
                .isbn(book.getIsbn())
                .title(book.getTitle())
                .description(book.getDescription())
                .publicationDate(book.getPublicationDate())
                .genre(genre.getName())
                .bookCopies(bookCopies)
                .authors(authors.stream().map(this::mapToDTO).toList())
                .build();
    }

    private AuthorDTO mapToDTO(Author author) {
        return AuthorDTO.builder()
                .id(author.getId())
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .biography(author.getBiography())
                .nationality(author.getNationality())
                .build();
    }

    private MemberDTO mapToDTO(Member member) {
        return MemberDTO.builder()
                .id(member.getId())
                .firstName(member.getFirstName())
                .lastName(member.getLastName())
                .dui(member.getDui())
                .email(member.getEmail())
                .createdOn(member.getCreatedOn())
                .isActive(member.isActive())
                .build();
    }

    private LibrarianDTO mapToDTO(Librarian librarian) {
        return LibrarianDTO
                .builder()
                .id(librarian.getId())
                .firstName(librarian.getFirstName())
                .lastName(librarian.getLastName())
                .dui(librarian.getDui())
                .email(librarian.getEmail())
                .phoneNumber(librarian.getPhoneNumber())
                .address(librarian.getAddress())
                .hiredOn(librarian.getHiredOn())
                .librarianRole(librarian.getLibrarianRole().name())
                .isActive(librarian.isActive())
                .build();
    }
}
