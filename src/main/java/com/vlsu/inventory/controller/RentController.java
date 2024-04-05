package com.vlsu.inventory.controller;

import com.vlsu.inventory.model.Rent;
import com.vlsu.inventory.security.UserDetailsImpl;
import com.vlsu.inventory.service.RentService;
import com.vlsu.inventory.util.exception.ActionNotAllowedException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/v1")
public class RentController {
    private final RentService rentService;

    public RentController(RentService rentService) {
        this.rentService = rentService;
    }

    @GetMapping("/rents")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LABHEAD')")
    public ResponseEntity<?> getAllByParams(
            @RequestParam(required = false) LocalDateTime createDateTimeFrom,
            @RequestParam(required = false) LocalDateTime createDateTimeTo,
            @RequestParam(required = false) LocalDateTime endDateTimeFrom,
            @RequestParam(required = false) LocalDateTime endDateTimeTo,
            @RequestParam(required = false) Boolean isClosed,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size) {
        try {
            List<Rent> rents = rentService.getAllRentsByParams(createDateTimeFrom, createDateTimeTo, endDateTimeFrom, endDateTimeTo, isClosed);
            return new ResponseEntity<>(RentService.getRentsPageable(rents, page, size), HttpStatus.OK);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/rents/my")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> getRentsOfUser(
            @AuthenticationPrincipal UserDetailsImpl principal,
            @RequestParam Boolean isClosed) {
        try {
            return new ResponseEntity<>(rentService.getRentsByUser(principal, isClosed), HttpStatus.OK);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/placements/{placementId}/rents")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LABHEAD')")
    public ResponseEntity<?> getRentsByPlacementId(
            @PathVariable Long placementId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size) {
        try {
            List<Rent> rents = rentService.getRentsByPlacementId(placementId);
            return new ResponseEntity<>(RentService.getRentsPageable(rents, page, size), HttpStatus.OK);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/responsible/{responsibleId}/rents")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LABHEAD')")
    public ResponseEntity<?> getRentsByResponsibleId(
            @PathVariable Long responsibleId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size) {
        try {
            List<Rent> rents = rentService.getRentsByResponsibleId(responsibleId);
            return new ResponseEntity<>(RentService.getRentsPageable(rents, page, size), HttpStatus.OK);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/equipment/{equipmentId}/rents")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LABHEAD')")
    public ResponseEntity<?> getRentsByEquipmentId(
            @PathVariable Long equipmentId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size) {
        try {
            List<Rent> rents = rentService.getRentsByEquipmentId(equipmentId);
            return new ResponseEntity<>(RentService.getRentsPageable(rents, page, size), HttpStatus.OK);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/rents")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> createRent(
            @RequestParam Long equipmentId,
            @RequestParam Long placementId,
            @AuthenticationPrincipal UserDetailsImpl principal,
            @RequestBody Rent rent) {
        try {
            rentService.createRent(equipmentId, placementId, principal, rent);
            return new ResponseEntity<>(rent, HttpStatus.CREATED);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ActionNotAllowedException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.LOCKED);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/rents/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> closeRent(@PathVariable Long id) {
        try {
            rentService.closeRentById(id);
            return new ResponseEntity<>("Взятие завершено", HttpStatus.OK);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/equipment/{equipmentId}/rents")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LABHEAD')")
    public ResponseEntity<?> deleteRentsByEquipmentId(@PathVariable Long equipmentId) {
        try {
            rentService.deleteRentByEquipmentId(equipmentId);
            return new ResponseEntity<>("Данные были успешно удалены", HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ActionNotAllowedException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.LOCKED);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/rents/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LABHEAD')")
    public ResponseEntity<?> deleteRentById(@PathVariable Long id) {
        try {
            rentService.deleteRentById(id);
            return new ResponseEntity<>("Данные были успешно удалены", HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ActionNotAllowedException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.LOCKED);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
