package com.tenniscourts.guests;

import com.tenniscourts.exceptions.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class GuestService {
    private final GuestRepository guestRepository;
    private final GuestMapper guestMapper;

    public GuestDTO addGuest(GuestDTO guest) {
        try {
            return guestMapper.map(guestRepository.saveAndFlush(guestMapper.map(guest)));
        } catch (Exception ex) {
            log.error("Exception occured while savig the guest details {}",ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public GuestDTO findByGuestId(Long id) {
        try {
            return guestRepository.findById(id).map(guestMapper::map).orElseThrow(() ->
                    new EntityNotFoundException("Guest not found."));
        } catch (Exception ex) {
            log.error("Exception occured while finding the guest by Id# {}, details {}", id, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public List<GuestDTO> findByGuestName(String name) {
        try {
            return guestRepository.findByName(name.toUpperCase()).stream().map(guestMapper::map).collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Exception occured while finding the guest by name# {}, details {}", name, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public List<GuestDTO> findAllGuests() {
        try {
            return guestRepository.findAll().stream().map(guestMapper::map).collect(Collectors.toList());
        } catch (Exception ex) {
            log.error("Exception occured while finding all guests {}", ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public GuestDTO updateGuest(Long id, GuestDTO guest) {
        try {
            if (guestRepository.existsById(id)) {
                guest.setId(id);
                return guestMapper.map(guestRepository.save(guestMapper.map(guest)));
            } else {
                log.error("Guest not found to update for given id# {}", id);
                throw new EntityNotFoundException("Guest not found.");
            }
        } catch (Exception ex) {
            log.error("Exception occured while updating the guest id# {}, details {}", id, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public String deleteGuestByGuestId(Long id) {
        try {
            if (guestRepository.existsById(id)) {
                guestRepository.deleteById(id);
                return "Successfully deleted guest.";
            } else {
                log.error("Guest not found for given id# {}", id);
                throw new EntityNotFoundException("Guest not found.");
            }
        } catch (Exception ex) {
            log.error("Exception occured while deleting the guest id# {}, details {}", id, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }

    public boolean isGuestExist(Long id) {
        try {
            return guestRepository.existsById(id) ? true : false;
        } catch (Exception ex) {
            log.error("Exception occured while validating the guest id# {} already exist {}", id, ex);
            throw new RuntimeException(ex.getMessage());
        }
    }
}
