package com.app.services;

import com.app.models.dtos.ReservationDTO;
import com.app.models.requests.CreateReservationRequest;

import java.time.LocalDate;
import java.util.List;

public interface IReservationService {
    ReservationDTO save(CreateReservationRequest reservation);
    ReservationDTO findById(Long id);
    List<ReservationDTO> findAll();
    List<ReservationDTO> findAllActiveReservations();
    List<ReservationDTO> findAllByMemberId(Long memberId);
    List<ReservationDTO> findAllByBookId(Long bookId);
    List<ReservationDTO> findAllByReservationDateBetween(LocalDate startDate, LocalDate endDate);
    List<ReservationDTO> findAllByStatus(String status);
    ReservationDTO update(Long id, String status);
}
