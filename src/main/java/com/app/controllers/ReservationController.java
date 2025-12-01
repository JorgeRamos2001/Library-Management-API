package com.app.controllers;

import com.app.models.dtos.ReservationDTO;
import com.app.models.requests.CreateReservationRequest;
import com.app.services.IReservationService;
import com.app.services.implementations.ReservationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final IReservationService reservationService;

    @PostMapping
    public ResponseEntity<ReservationDTO> save(@Valid @RequestBody CreateReservationRequest reservation) {
        return new ResponseEntity<>(reservationService.save(reservation), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservationDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(reservationService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ReservationDTO>> findAll(){
        return ResponseEntity.ok(reservationService.findAll());
    }

    @GetMapping("/active")
    public ResponseEntity<List<ReservationDTO>> findAllActiveReservations(){
        return ResponseEntity.ok(reservationService.findAllActiveReservations());
    }

    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<ReservationDTO>> findAllByMemberId(@PathVariable Long memberId){
        return ResponseEntity.ok(reservationService.findAllByMemberId(memberId));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<ReservationDTO>> findAllByBookId(@PathVariable Long bookId){
        return ResponseEntity.ok(reservationService.findAllByBookId(bookId));
    }

    @GetMapping("/range")
    public ResponseEntity<List<ReservationDTO>> findByDateRange(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
    ) {
        return ResponseEntity.ok(reservationService.findAllByReservationDateBetween(startDate, endDate));
    }

    @GetMapping("/status")
    public ResponseEntity<List<ReservationDTO>> findAllByStatus(@RequestParam String status){
        return ResponseEntity.ok(reservationService.findAllByStatus(status));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReservationDTO> update(@PathVariable Long id, @RequestParam String status){
        return ResponseEntity.ok(reservationService.update(id, status));
    }

}
