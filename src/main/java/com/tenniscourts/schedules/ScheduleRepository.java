package com.tenniscourts.schedules;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {

    List<Schedule> findByTennisCourt_IdOrderByStartDateTime(Long id);

    @Query(value = "SELECT * FROM SCHEDULE WHERE START_DATE_TIME >= ?1 AND END_DATE_TIME <= ?2", nativeQuery = true)
    List<Schedule> findByStartDateAndEndDate(LocalDateTime startDate, LocalDateTime endDate);
}