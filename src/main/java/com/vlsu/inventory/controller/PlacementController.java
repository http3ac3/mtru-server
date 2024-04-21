package com.vlsu.inventory.controller;

import com.vlsu.inventory.dto.model.PlacementDto;
import com.vlsu.inventory.model.Placement;
import com.vlsu.inventory.service.PlacementService;
import com.vlsu.inventory.util.exception.ResourceHasDependenciesException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("api/v1")
public class PlacementController {

    PlacementService placementService;

    @GetMapping("/placements")
    public ResponseEntity<List<PlacementDto.Response.Default>> getAllPlacements() {
        return ResponseEntity.ok(placementService.getAll());
    }


    @GetMapping("/placements/{id}")
    public ResponseEntity<PlacementDto.Response.Default> getPlacementById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(placementService.getById(id));
        }
        catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/placements")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Placement> create(@RequestBody PlacementDto.Request.Create request) {
        try {
            Placement created = placementService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/placements")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> update(@RequestBody PlacementDto.Request.Update request) {
        try {
            Placement updated = placementService.update(request);
            return ResponseEntity.ok(updated);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/placements/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deletePlacementById(
            @PathVariable Long id) {
        try {
            placementService.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body("Данные были успешно удалены");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ResourceHasDependenciesException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }



}
