package com.tenniscourts.reservations;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.guests.GuestService;
import com.tenniscourts.schedules.ScheduleService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Service
@AllArgsConstructor
@Slf4j
public class ReservationService {

    private final ReservationRepository reservationRepository;

    private final ReservationMapper reservationMapper;

    private final GuestService guestService;

    private final ScheduleService scheduleService;

    public ReservationDTO bookReservation(CreateReservationRequestDTO createReservationRequestDTO) {
        try {
            if (!guestService.isGuestExist(createReservationRequestDTO.getGuestId())) {
                throw new EntityNotFoundException("Guest not found.");
            } else if (!scheduleService.isScheduleExist(createReservationRequestDTO.getScheduleId())) {
                throw new EntityNotFoundException("Schedule not found.");
            }
            ReservationDTO reservationDTO = ReservationDTO.builder()
                    .guest(guestService.findByGuestId(createReservationRequestDTO.getGuestId()))
                    .guestId(createReservationRequestDTO.getGuestId())
                    .schedule(scheduleService.findSchedule(createReservationRequestDTO.getScheduleId()))
                    .scheduledId(createReservationRequestDTO.getScheduleId())
                    .value(new BigDecimal(10))
                    .reservationStatus(ReservationStatus.READY_TO_PLAY.name()).build();
            return reservationMapper.map(reservationRepository.saveAndFlush(reservationMapper.map(reservationDTO)));
        } catch (Exception ex) {
            log.error("Exception occured in booking reservation {}", ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public ReservationDTO findReservation(Long reservationId) {
        try {
            return reservationRepository.findById(reservationId).map(reservationMapper::map).orElseThrow(() ->
                    new EntityNotFoundException("Reservation not found.")
            );
        } catch (Exception ex) {
            log.error("Exception occured in finding reservation {}", ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public ReservationDTO cancelReservation(Long reservationId) {
        try {
            return reservationMapper.map(this.cancel(reservationId));
        } catch (Exception ex) {
            log.error("Exception occured in cancel reservation {}", ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    private Reservation cancel(Long reservationId) {
        try {
            return reservationRepository.findById(reservationId).map(reservation -> {

                this.validateCancellation(reservation);

                BigDecimal refundValue = getRefundValue(reservation);
                return this.updateReservation(reservation, refundValue, ReservationStatus.CANCELLED);

            }).orElseThrow(() -> new EntityNotFoundException("Reservation not found."));
        } catch (Exception ex) {
            log.error("Exception occured in cancel reservation {}", ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    private Reservation updateReservation(Reservation reservation, BigDecimal refundValue, ReservationStatus status) {
        try {
            reservation.setReservationStatus(status);
            reservation.setValue(reservation.getValue().subtract(refundValue));
            reservation.setRefundValue(refundValue);

            return reservationRepository.save(reservation);
        } catch (Exception ex) {
            log.error("Exception occured in update Reservation {}", ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    private void validateCancellation(Reservation reservation) {
        try {
            if (!ReservationStatus.READY_TO_PLAY.equals(reservation.getReservationStatus())) {
                throw new IllegalArgumentException("Cannot cancel/reschedule because it's not in ready to play status.");
            }

            if (reservation.getSchedule().getStartDateTime().isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException("Can cancel/reschedule only future dates.");
            }
        } catch (Exception ex) {
            log.error("Exception occured in validate Cancellation {}", ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public BigDecimal getRefundValue(Reservation reservation) {
        try {
            long hours = ChronoUnit.HOURS.between(LocalDateTime.now(), reservation.getSchedule().getStartDateTime());

            if (hours >= 24) {
                return reservation.getValue();
            }

            return BigDecimal.ZERO;
        } catch (Exception ex) {
            log.error("Exception occured in get RefundValue {}", ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    /*TODO: This method actually not fully working, find a way to fix the issue when it's throwing the error:
            "Cannot reschedule to the same slot.*/
    public ReservationDTO rescheduleReservation(Long previousReservationId, Long scheduleId) {
        try {
            Reservation previousReservation = cancel(previousReservationId);

            if (scheduleId.equals(previousReservation.getSchedule().getId())) {
                throw new IllegalArgumentException("Cannot reschedule to the same slot.");
            }

            previousReservation.setReservationStatus(ReservationStatus.RESCHEDULED);
            reservationRepository.save(previousReservation);

            ReservationDTO newReservation = bookReservation(CreateReservationRequestDTO.builder()
                    .guestId(previousReservation.getGuest().getId())
                    .scheduleId(scheduleId)
                    .build());
            newReservation.setPreviousReservation(reservationMapper.map(previousReservation));
            return newReservation;
        } catch (Exception ex) {
            log.error("Exception occured in reschedule Reservation {}", ex);
            throw new RuntimeException(ex.getMessage());
        }
    }
}
