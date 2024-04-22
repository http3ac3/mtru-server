package com.vlsu.inventory.controller;

import com.vlsu.inventory.dto.model.RentDto;
import com.vlsu.inventory.model.Rent;
import com.vlsu.inventory.model.User;
import com.vlsu.inventory.security.UserDetailsImpl;
import com.vlsu.inventory.service.RentService;
import com.vlsu.inventory.util.exception.ActionNotAllowedException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("api/v1")
public class RentController {

    RentService rentService;

    // TODO Create Rent filter request DTO
    @GetMapping("/rents")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LABHEAD')")
    public ResponseEntity<?> getAllByParams(
            @RequestParam(required = false) LocalDateTime createDateTimeFrom,
            @RequestParam(required = false) LocalDateTime createDateTimeTo,
            @RequestParam(required = false) LocalDateTime endDateTimeFrom,
            @RequestParam(required = false) LocalDateTime endDateTimeTo,
            @RequestParam(required = false) Boolean isClosed,
            @RequestParam(required = false) Long equipmentId,
            @RequestParam(required = false) Long responsibleId,
            @RequestParam(required = false) Long placementId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size) {
        try {
            List<RentDto.Response.Default> rents = rentService.getAllByParams(
                    createDateTimeFrom, createDateTimeTo, endDateTimeFrom, endDateTimeTo,
                    isClosed, equipmentId, responsibleId, placementId);
            return ResponseEntity.ok(rents);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/rents/my")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getRentsOfUser(
            @AuthenticationPrincipal User principal) {
        try {
            return ResponseEntity.ok(rentService.getUnclosedByUser(principal));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/rents")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> create(
            @AuthenticationPrincipal User principal,
            @RequestBody RentDto.Request.Create request) {
        try {
            return ResponseEntity.ok(rentService.create(request, principal));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ActionNotAllowedException e) {
            return ResponseEntity.status(HttpStatus.LOCKED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/rents/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> close(@PathVariable Long id) {
        try {
            rentService.closeRent(id);
            return ResponseEntity.ok("Взятие завершено");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/equipment/{equipmentId}/rents")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LABHEAD')")
    public ResponseEntity<?> deleteRentsByEquipmentId(@PathVariable Long equipmentId) {
        try {
            rentService.deleteRentByEquipmentId(equipmentId);
            return new ResponseEntity<>("Данные были успешно удалены", HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ActionNotAllowedException e) {
            return ResponseEntity.status(HttpStatus.LOCKED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/rents/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LABHEAD')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            rentService.delete(id);
            return ResponseEntity.ok("Данные были успешно удалены");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ActionNotAllowedException e) {
            return ResponseEntity.status(HttpStatus.LOCKED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
