package com.tenniscourts.tenniscourts;

import com.tenniscourts.config.BaseRestController;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tenniscourt")
@AllArgsConstructor
public class TennisCourtController extends BaseRestController {

    private final TennisCourtService tennisCourtService;

    @PostMapping
    public ResponseEntity<?> addTennisCourt(@RequestBody TennisCourtDTO tennisCourtDTO) {
        return StringUtils.hasText(tennisCourtDTO.getName()) ? ResponseEntity.created(locationByEntity(tennisCourtService.addTennisCourt(tennisCourtDTO).getId())).build()
                : ResponseEntity.badRequest().body("Name is not valid.");
    }

    @GetMapping("/{id}")
    public ResponseEntity<TennisCourtDTO> findTennisCourtById(@PathVariable(name = "id") Long tennisCourtId) {
        return ResponseEntity.ok(tennisCourtService.findTennisCourtById(tennisCourtId));
    }

    @GetMapping("/{id}/schedule")
    public ResponseEntity<TennisCourtDTO> findTennisCourtWithSchedulesById(@PathVariable(name = "id") Long tennisCourtId) {
        return ResponseEntity.ok(tennisCourtService.findTennisCourtWithSchedulesById(tennisCourtId));
    }
}
