package com.app.repositories;

import com.app.models.entities.Book;
import com.app.models.entities.Member;
import com.app.models.entities.Reservation;
import com.app.models.enums.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface IReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findAllByMember(Member member);
    List<Reservation> findAllByBook(Book book);
    List<Reservation> findAllByReservationDateBetween(LocalDate startDate, LocalDate endDate);
    List<Reservation> findAllByStatus(ReservationStatus status);
    List<Reservation> findByMemberAndBook(Member member, Book book);
}
