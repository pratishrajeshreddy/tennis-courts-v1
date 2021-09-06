package com.tenniscourts.schedules;

import com.tenniscourts.exceptions.EntityNotFoundException;
import com.tenniscourts.tenniscourts.TennisCourtDTO;
import com.tenniscourts.tenniscourts.TennisCourtMapper;
import com.tenniscourts.tenniscourts.TennisCourtRepository;
import com.tenniscourts.tenniscourts.TennisCourtService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    private final TennisCourtRepository tennisCourtRepository;

    private final TennisCourtMapper tennisCourtMapper;

    private final ScheduleMapper scheduleMapper;

    public ScheduleDTO addSchedule(Long tennisCourtId, CreateScheduleRequestDTO createScheduleRequestDTO) {
        if(tennisCourtRepository.existsById(tennisCourtId)) {
            TennisCourtDTO tennisCourtDTO = tennisCourtMapper.map(tennisCourtRepository.findById(tennisCourtId).get());
            ScheduleDTO scheduleDTO = ScheduleDTO.builder().tennisCourtId(tennisCourtId).tennisCourt(tennisCourtDTO).startDateTime(createScheduleRequestDTO.getStartDateTime())
                    .endDateTime(createScheduleRequestDTO.getStartDateTime().plusHours(1)).build();
            return scheduleMapper.map(scheduleRepository.saveAndFlush(scheduleMapper.map(scheduleDTO)));
        } else {
            throw new EntityNotFoundException("Tennicourt not found.");
        }
    }

    public List<ScheduleDTO> findSchedulesByDates(LocalDateTime startDate, LocalDateTime endDate) {
        return scheduleRepository.findByStartDateAndEndDate(startDate, endDate).stream().map(scheduleMapper::map).collect(Collectors.toList());
    }

    public ScheduleDTO findSchedule(Long scheduleId) {
        return scheduleRepository.findById(scheduleId).map(scheduleMapper::map).orElseThrow(() -> new EntityNotFoundException("Schedule not found."));
    }

    public List<ScheduleDTO> findSchedulesByTennisCourtId(Long tennisCourtId) {
        return scheduleMapper.map(scheduleRepository.findByTennisCourt_IdOrderByStartDateTime(tennisCourtId));
    }

    public boolean isScheduleExist(Long id) {
        return scheduleRepository.existsById(id) ? true : false;
    }
}
