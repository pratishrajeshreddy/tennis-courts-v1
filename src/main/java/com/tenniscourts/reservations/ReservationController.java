package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/reservation")
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Void> bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {
        return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @GetMapping(value = "/{reservation-id}")
    public ResponseEntity<ReservationDTO> findReservation(@PathVariable(name = "reservation-id") Long reservationId) {
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    @PutMapping(value = "/{reservation-id}/cancel")
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable(name = "reservation-id") Long reservationId) {
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @PutMapping(value = "/{reservation-id}/reschedule/{schedule-id}")
    public ResponseEntity<ReservationDTO> rescheduleReservation(@PathVariable(name = "reservation-id") Long reservationId, @PathVariable(name = "schedule-id") Long scheduleId) {
        return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
    }
}
