package com.tenniscourts.reservations;

import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/reservation")
@Slf4j
public class ReservationController extends BaseRestController {

    private final ReservationService reservationService;

    @PostMapping
    public ResponseEntity<Void> bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {
        log.info("bookReservation request# {}", createReservationRequestDTO);
        return ResponseEntity.created(locationByEntity(reservationService.bookReservation(createReservationRequestDTO).getId())).build();
    }

    @GetMapping(value = "/{reservation-id}")
    public ResponseEntity<ReservationDTO> findReservation(@PathVariable(name = "reservation-id") Long reservationId) {
        log.info("findReservation id# {}", reservationId);
        return ResponseEntity.ok(reservationService.findReservation(reservationId));
    }

    @PutMapping(value = "/{reservation-id}/cancel")
    public ResponseEntity<ReservationDTO> cancelReservation(@PathVariable(name = "reservation-id") Long reservationId) {
        log.info("cancelReservation id# {}", reservationId);
        return ResponseEntity.ok(reservationService.cancelReservation(reservationId));
    }

    @PutMapping(value = "/{reservation-id}/reschedule/{schedule-id}")
    public ResponseEntity<ReservationDTO> rescheduleReservation(@PathVariable(name = "reservation-id") Long reservationId, @PathVariable(name = "schedule-id") Long scheduleId) {
        log.info("rescheduleReservation reservationId# {}, scheduleId# {}", reservationId, scheduleId);
        return ResponseEntity.ok(reservationService.rescheduleReservation(reservationId, scheduleId));
    }
}
