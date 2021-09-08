package com.tenniscourts.guests;

import com.tenniscourts.config.BaseRestController;
import com.tenniscourts.exceptions.EntityNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/guest")
@Slf4j
@Api(value = "guest", description = "Guest registration")
@ApiResponses(value = {
        @ApiResponse(code = 201, message = "Successfully added"),
        @ApiResponse(code = 400, message = "The name you were trying to add is not valid"),
        @ApiResponse(code = 404, message = "The resource you were trying to reach is not found"),
        @ApiResponse(code = 500, message = "Internal Server Error")
})
public class GuestController extends BaseRestController {

    private final GuestService guestService;

    @ApiOperation(value = "Add a guest")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Successfully added guest"),
            @ApiResponse(code = 400, message = "The name you were trying to add is not valid")
    })
    @PostMapping
    public ResponseEntity<?> addGuest(@RequestBody GuestDTO guest) {
        log.info("addGuest request {}", guest);
        return StringUtils.hasText(guest.getName()) ? ResponseEntity.ok(guestService.addGuest(guest)) : ResponseEntity.badRequest().body("Name is not valid.");
    }

    @ApiOperation(value = "Find guest with an ID")
    @GetMapping(value = "/{id}")
    public ResponseEntity<?> findGuestById(@Valid @PathVariable(name = "id") Long id) {
        log.info("findGuestById id# {}", id);
        if(guestService.isGuestExist(id)) {
            return ResponseEntity.ok(guestService.findByGuestId(id));
        } else {
            return ResponseEntity.badRequest().body("Guest not found.");
        }
    }

    @ApiOperation(value = "Find guest with Name")
    @GetMapping(value = "/name/{name}")
    public ResponseEntity<?> findGuestByName(@Valid @PathVariable(name = "name") String name) {
        log.info("findGuestByName name# {}", name);
        return StringUtils.hasText(name) ? ResponseEntity.ok(guestService.findByGuestName(name)) : ResponseEntity.badRequest().body("Name is not valid.");
    }

    @ApiOperation(value = "Find all Guests")
    @GetMapping
    public ResponseEntity<?> findAllGuest() {
        log.info("findAllGuest ");
        return ResponseEntity.ok(guestService.findAllGuests());
    }

    @ApiOperation(value = "Update guest with an Id")
    @PutMapping(value = "/{id}")
    public ResponseEntity<?> updateGuest(@Valid @PathVariable(name = "id") Long id, @RequestBody GuestDTO guest) {
        log.info("updateGuest id# {}, guest# {}", id, guest);
        return StringUtils.hasText(guest.getName()) ? ResponseEntity.ok(guestService.updateGuest(id, guest)) : ResponseEntity.badRequest().body("Name is not valid.");
    }

    @ApiOperation(value = "Delete guest with an Id")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteGuest(@Valid @PathVariable(name = "id") Long id) {
        log.info("deleteGuest id# {}",id);
        try {
            return ResponseEntity.ok(guestService.deleteGuestByGuestId(id));
        } catch (EntityNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
