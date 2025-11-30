package com.app.services.implementations;

import com.app.exceptions.BadRequestException;
import com.app.exceptions.OperationNotPermittedException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.models.dtos.AuthorDTO;
import com.app.models.dtos.BookDTO;
import com.app.models.dtos.MemberDTO;
import com.app.models.dtos.ReservationDTO;
import com.app.models.entities.*;
import com.app.models.enums.BookCondition;
import com.app.models.enums.ReservationStatus;
import com.app.models.requests.CreateReservationRequest;
import com.app.repositories.IBookCopyRepository;
import com.app.repositories.IBookRepository;
import com.app.repositories.IMemberRepository;
import com.app.repositories.IReservationRepository;
import com.app.services.IReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ReservationServiceImpl implements IReservationService {
    private final IReservationRepository reservationRepository;
    private final IBookRepository bookRepository;
    private final IBookCopyRepository bookCopyRepository;
    private final IMemberRepository memberRepository;

    @Override
    public ReservationDTO save(CreateReservationRequest reservation) {
        Member member = memberRepository.findById(reservation.getMemberId()).orElseThrow(() -> new ResourceNotFoundException("Member with id: " + reservation.getMemberId() + " not found."));
        Book book = bookRepository.findById(reservation.getBookId()).orElseThrow(() -> new ResourceNotFoundException("Book with id: " + reservation.getBookId() + " not found."));

        if(reservationRepository.existsByMemberAndBookAndStatus(member, book, ReservationStatus.PENDING)) throw new OperationNotPermittedException("Member with id: " + reservation.getMemberId() + " already has a reservation for book with id: " + reservation.getBookId());
        if(bookCopyRepository.countByBookAndCondition(book, BookCondition.ACTIVE) == 0) throw new OperationNotPermittedException("Book with id: " + reservation.getBookId() + " does not have copies available.");

        return mapToDTO(reservationRepository.save(Reservation
                .builder()
                .member(member)
                .book(book)
                .reservationDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(3))
                .status(ReservationStatus.PENDING)
                .build())
        );
    }

    @Override
    public ReservationDTO findById(Long id) {
        return mapToDTO(reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation with id: " + id + " not found.")));
    }

    @Override
    public List<ReservationDTO> findAll() {
        return reservationRepository.findAll().stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<ReservationDTO> findAllActiveReservations() {
        return reservationRepository.findAllByStatus(ReservationStatus.PENDING).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<ReservationDTO> findAllByMemberId(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("Member with id: " + memberId + " not found."));
        return reservationRepository.findAllByMember(member).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<ReservationDTO> findAllByBookId(Long bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book with id: " + bookId + " not found."));
        return reservationRepository.findAllByBook(book).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<ReservationDTO> findAllByReservationDateBetween(LocalDate startDate, LocalDate endDate) {
        return reservationRepository.findAllByReservationDateBetween(startDate, endDate).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<ReservationDTO> findAllByStatus(String status) {
        try {
            ReservationStatus reservationStatus = ReservationStatus.valueOf(status.toUpperCase());
            return reservationRepository.findAllByStatus(reservationStatus).stream()
                    .map(this::mapToDTO)
                    .toList();
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status: " + status + ". Valid statuses: PENDING, CANCELED, COMPLETED, OVERDUE");
        }
    }

    @Override
    public ReservationDTO update(Long id, String status) {
        try {
            ReservationStatus reservationStatus = ReservationStatus.valueOf(status.toUpperCase());
            Reservation reservationToUpdate = reservationRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Reservation with id: " + id + " not found."));
            reservationToUpdate.setStatus(reservationStatus);
            return mapToDTO(reservationRepository.save(reservationToUpdate));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status: " + status + ". Valid statuses: PENDING, CANCELED, COMPLETED, OVERDUE");
        }
    }

    private ReservationDTO mapToDTO(Reservation reservation) {
        return ReservationDTO
                .builder()
                .id(reservation.getId())
                .reservationDate(reservation.getReservationDate())
                .dueDate(reservation.getDueDate())
                .status(reservation.getStatus().name())
                .member(mapToDTO(reservation.getMember()))
                .book(mapToDTO(reservation.getBook(), reservation.getBook().getAuthors(), reservation.getBook().getGenre(), (long) bookCopyRepository.countByBookAndCondition(reservation.getBook(), BookCondition.ACTIVE)))
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
}
