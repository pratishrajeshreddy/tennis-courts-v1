package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class GuestService {
    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    public GuestDTO addGuest(GuestDTO guest) {
        return guestMapper.map(guestRepository.saveAndFlush(guestMapper.map(guest)));
    }

    public GuestDTO findByGuestId(Long id) {
        return guestRepository.findById(id).map(guestMapper::map).orElseThrow(() -> new EntityNotFoundException("Guest not found."));
    }

    public List<GuestDTO> findByGuestName(String name) {
        return guestRepository.findByName(name.toUpperCase()).stream().map(guestMapper::map).collect(Collectors.toList());
    }

    public List<GuestDTO> findAllGuests() {
        return guestRepository.findAll().stream().map(guestMapper::map).collect(Collectors.toList());
    }

    public GuestDTO updateGuest(Long id, GuestDTO guest) {
        if(guestRepository.existsById(id)) {
            guest.setId(id);
            return guestMapper.map(guestRepository.save(guestMapper.map(guest)));
        } else {
            throw new EntityNotFoundException("Guest not found.");
        }
    }

    public String deleteGuestByGuestId(Long id) {
        if (guestRepository.existsById(id)) {
            guestRepository.deleteById(id);
            return "Successfully deleted guest.";
        } else {
            throw new EntityNotFoundException("Guest not found.");
        }
    }

    public boolean isGuestExist(Long id) {
        return guestRepository.existsById(id) ? true : false;
    }
}
