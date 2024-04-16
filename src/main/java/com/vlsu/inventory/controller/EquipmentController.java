package com.vlsu.inventory.controller;

import com.vlsu.inventory.model.Equipment;
import com.vlsu.inventory.model.User;
import com.vlsu.inventory.security.UserDetailsImpl;
import com.vlsu.inventory.service.EquipmentService;
import com.vlsu.inventory.util.exception.ActionNotAllowedException;
import com.vlsu.inventory.util.exception.ResourceHasDependenciesException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("api/v1")
public class EquipmentController {
    EquipmentService equipmentService;

    @GetMapping("/equipment")
    public ResponseEntity<?> getAllEquipmentPageable(
            @RequestParam(required = false) String inventoryNumber,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) BigDecimal initialCostFrom,
            @RequestParam(required = false) BigDecimal initialCostTo,
            @RequestParam(required = false) LocalDate commissioningDateFrom,
            @RequestParam(required = false) LocalDate commissioningDateTo,
            @RequestParam(required = false) LocalDate decommissioningDateFrom,
            @RequestParam(required = false) LocalDate decommissioningDateTo,
            @RequestParam(required = false) String commissioningActNumber,
            @RequestParam(required = false) String decommissioningActNumber,
            @RequestParam(required = false) Long subcategoryId,
            @RequestParam(required = false) Long responsibleId,
            @RequestParam(required = false) Long placementId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size) {
        try {
            List<Equipment> equipment = equipmentService.getAllEquipmentByParams(inventoryNumber, name, initialCostFrom, initialCostTo,
                    commissioningDateFrom, commissioningDateTo, decommissioningDateFrom, decommissioningDateTo,
                    commissioningActNumber, decommissioningActNumber, subcategoryId, responsibleId, placementId);
            return new ResponseEntity<>(equipment, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/equipment/{id}")
    public ResponseEntity<?> getEquipmentById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(equipmentService.getEquipmentById(id), HttpStatus.OK);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/subcategories/{subcategoryId}/equipment")
    public ResponseEntity<?> getEquipmentBySubcategoryId(
            @PathVariable Long subcategoryId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size) {
        try {
            List<Equipment> equipment = equipmentService.getEquipmentBySubcategoryId(subcategoryId);
            return new ResponseEntity<>(EquipmentService.getEquipmentByPage(equipment, page, size), HttpStatus.OK);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/placements/{placementId}/equipment")
    public ResponseEntity<?> getEquipmentByPlacementId(
            @PathVariable Long placementId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size) {
        try {
            List<Equipment> equipment = equipmentService.getEquipmentByPlacementId(placementId);
            return new ResponseEntity<>(EquipmentService.getEquipmentByPage(equipment, page, size), HttpStatus.OK);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/responsible/{responsibleId}/equipment")
    public ResponseEntity<?> getEquipmentByResponsibleId(
            @PathVariable Long responsibleId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size) {
        try {
            List<Equipment> equipment = equipmentService.getEquipmentByResponsibleId(responsibleId);
            return new ResponseEntity<>(EquipmentService.getEquipmentByPage(equipment, page, size), HttpStatus.OK);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/equipment")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LABHEAD')")
    public ResponseEntity<?> createEquipment(
            @RequestBody Equipment equipment,
            @AuthenticationPrincipal User principal) {
        try {
            equipmentService.createEquipment(equipment.getSubcategory().getId(),
                    equipment.getResponsible().getId(), equipment.getPlacement().getId(), equipment, principal);
            return new ResponseEntity<>(equipment, HttpStatus.CREATED);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ActionNotAllowedException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.LOCKED);
        } catch (Exception exception) {
            System.out.println(exception.getMessage());
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/equipment/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LABHEAD')")
    public ResponseEntity<String> updateEquipmentById(
            @PathVariable Long id,
            @RequestBody Equipment equipment,
            @AuthenticationPrincipal User principal) {
        try {
            equipmentService.updateEquipmentById(id, equipment.getSubcategory().getId(),
                    equipment.getResponsible().getId(), equipment.getPlacement().getId(), equipment, principal);
            return new ResponseEntity<>("Данные были успешно обновлены", HttpStatus.OK);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ActionNotAllowedException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.LOCKED);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/equipment/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LABHEAD')")
    public ResponseEntity<String> deleteEquipmentById(
            @PathVariable Long id,
            @AuthenticationPrincipal User principal) {
        try {
            equipmentService.deleteEquipmentById(id, principal);
            return new ResponseEntity<>("Данные были успешно удалены", HttpStatus.NO_CONTENT);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ResourceHasDependenciesException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
        } catch (ActionNotAllowedException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.LOCKED);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
