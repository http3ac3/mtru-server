package com.vlsu.inventory.controller;

import com.vlsu.inventory.dto.ImageResponse;
import com.vlsu.inventory.dto.model.EquipmentDto;
import com.vlsu.inventory.model.User;
import com.vlsu.inventory.service.EquipmentService;
import com.vlsu.inventory.service.ExcelExportService;
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("api/v1")
public class EquipmentController {
    EquipmentService equipmentService;
    ExcelExportService excelExportService;

    // TODO Create Params Request DTO
    @GetMapping("/equipment")
    public ResponseEntity<?> getAll(
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
            List<EquipmentDto.Response.Default> equipment = equipmentService.getAllByParams(inventoryNumber, name, initialCostFrom, initialCostTo,
                    commissioningDateFrom, commissioningDateTo, decommissioningDateFrom, decommissioningDateTo,
                    commissioningActNumber, decommissioningActNumber, subcategoryId, responsibleId, placementId);
            return ResponseEntity.ok(equipment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/equipment/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(equipmentService.getById(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/equipment/{id}/image")
    public ResponseEntity<?> getImageById(@PathVariable Long id) {
        try {
            ImageResponse response = new ImageResponse(equipmentService.getBase64ImageById(id));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/equipment")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LABHEAD')")
    public ResponseEntity<?> create(
            @ModelAttribute EquipmentDto.Request.Create request,
            @AuthenticationPrincipal User principal) {
        try {
            equipmentService.create(request, principal);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ActionNotAllowedException e) {
            return ResponseEntity.status(HttpStatus.LOCKED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/equipment")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LABHEAD')")
    public ResponseEntity<?> update(
            @ModelAttribute EquipmentDto.Request.Update request,
            @AuthenticationPrincipal User principal) {
        try {
            equipmentService.update(request, principal);
            return ResponseEntity.ok().build();
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ActionNotAllowedException e) {
            return ResponseEntity.status(HttpStatus.LOCKED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/equipment/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_LABHEAD')")
    public ResponseEntity<String> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User principal) {
        try {
            equipmentService.delete(id, principal);
            return ResponseEntity.ok("Данные были успешно удалены");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ResourceHasDependenciesException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (ActionNotAllowedException e) {
            return ResponseEntity.status(HttpStatus.LOCKED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
