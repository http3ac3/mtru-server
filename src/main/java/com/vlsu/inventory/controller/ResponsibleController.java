package com.vlsu.inventory.controller;

import com.vlsu.inventory.model.Responsible;
import com.vlsu.inventory.service.ResponsibleService;
import com.vlsu.inventory.util.exception.ResourceHasDependenciesException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("api/v1")
public class ResponsibleController {
    private final ResponsibleService responsibleService;

    public ResponsibleController(ResponsibleService responsibleService) {
        this.responsibleService = responsibleService;
    }

    @GetMapping("/responsible")
    public ResponseEntity<?> getAllResponsible(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(required = false) Boolean isFinanciallyResponsible,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size) {
        try {
            page = page - 1;
            List<Responsible> responsibleList = responsibleService.getAllResponsible(firstName, lastName, isFinanciallyResponsible);
            return new ResponseEntity<>(ResponsibleService.getResponsibleByPage(responsibleList, page, size), HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/departments/{departmentId}/responsible")
    public ResponseEntity<?> getResponsibleByDepartmentId(@PathVariable Long departmentId) {
        try {
            return new ResponseEntity<>(responsibleService.getResponsibleByDepartmentId(departmentId), HttpStatus.OK);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/responsible/{id}")
    public ResponseEntity<?> getResponsibleById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(responsibleService.getResponsibleById(id), HttpStatus.OK);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/departments/{departmentId}/responsible")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createResponsible(
            @PathVariable Long departmentId,
            @RequestBody Responsible responsibleRequest) {
        try {
            responsibleService.createResponsible(departmentId, responsibleRequest);
            return new ResponseEntity<>(responsibleRequest, HttpStatus.CREATED);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/responsible/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateResponsibleById(
            @PathVariable Long id,
            @RequestParam Long departmentId,
            @RequestBody Responsible responsibleRequest) {
        try {
            responsibleService.updateResponsibleById(id, departmentId, responsibleRequest);
            return new ResponseEntity<>("Данные были успешно обновлены", HttpStatus.OK);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/responsible/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteResponsibleById(@PathVariable Long id) {
        try {
            responsibleService.deleteById(id);
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