package com.tenniscourts.guests;

import com.sun.media.sound.InvalidDataException;
import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/guest")
public class GuestController extends BaseRestController {

    private final GuestService guestService;

    @PostMapping
    public ResponseEntity<?> addGuest(@RequestBody GuestDTO guest) {
        return StringUtils.hasText(guest.getName()) ? ResponseEntity.ok(guestService.addGuest(guest)) : ResponseEntity.badRequest().body("Name is not valid.");
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findGuestById(@Valid @PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(guestService.findByGuestId(id));
    }

    @GetMapping(value = "/name/{name}")
    public ResponseEntity<?> findGuestByName(@Valid @PathVariable(name = "name") String name) {
        return StringUtils.hasText(name) ? ResponseEntity.ok(guestService.findByGuestName(name)) : ResponseEntity.badRequest().body("Name is not valid.");
    }

    @GetMapping
    public ResponseEntity<?> findAllGuest() {
        return ResponseEntity.ok(guestService.findAllGuests());
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateGuest(@Valid @PathVariable(name = "id") Long id, GuestDTO guest) {
        return StringUtils.hasText(guest.getName()) ? ResponseEntity.ok(guestService.updateGuest(id, guest)) : ResponseEntity.badRequest().body("Name is not valid.");
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteGuest(@Valid @PathVariable(name = "id") Long id) {
        return ResponseEntity.ok(guestService.deleteGuestByGuestId(id));
    }
}
