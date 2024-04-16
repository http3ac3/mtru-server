package com.vlsu.inventory.controller;

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

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("api/v1")
public class PlacementController {

    PlacementService placementService;

    @GetMapping("/placements/all")
    public ResponseEntity<List<Placement>> getAllPlacements() {
        return new ResponseEntity<>(placementService.getAllPlacements(), HttpStatus.OK);
    }


    @GetMapping("/placements/{id}")
    public ResponseEntity<Placement> getPlacementById(@PathVariable Long id) {
        try {
            Placement placement = placementService.getPlacementById(id);
            return new ResponseEntity<>(placement, HttpStatus.OK);
        }
        catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/placements")
    public ResponseEntity<List<Placement>> getPlacementByName(
            @RequestParam String name) {
        List<Placement> placements = placementService.getPlacementByName(name);
        return new ResponseEntity<>(placements, HttpStatus.OK);
    }

    @PostMapping("/placements")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Placement> createPlacement(
            @RequestBody Placement placement) {
        try {
            placementService.createPlacement(placement);
            return new ResponseEntity<>(placement, HttpStatus.CREATED);
        } catch (Exception ex) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/placements/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updatePlacementById(
            @PathVariable Long id,
            @RequestBody Placement placementRequest) {
        try {
            placementService.updatePlacementById(id, placementRequest);
            return new ResponseEntity<>("Данные были успешно обновлены", HttpStatus.OK);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/placements/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deletePlacementById(
            @PathVariable Long id) {
        try {
            placementService.deletePlacementById(id);
            return new ResponseEntity<>("Данные были успешно удалены", HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ResourceHasDependenciesException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }



}
