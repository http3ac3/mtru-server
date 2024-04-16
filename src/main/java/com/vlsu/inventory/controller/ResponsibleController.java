package com.vlsu.inventory.controller;

import com.vlsu.inventory.model.Department;
import com.vlsu.inventory.model.Responsible;
import com.vlsu.inventory.service.ResponsibleService;
import com.vlsu.inventory.util.exception.ResourceHasDependenciesException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("api/v1")
public class ResponsibleController {

    ResponsibleService responsibleService;

    @GetMapping("/responsible")
    public ResponseEntity<?> getAllResponsible(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) Boolean isFinanciallyResponsible,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Boolean hasAccount) {
        try {
            page = page - 1;
            List<Responsible> responsibleList = responsibleService.getAllResponsible(firstName, lastName, isFinanciallyResponsible, departmentId);

            if (hasAccount != null && !hasAccount) {
                List<Responsible> withoutAccountResponsible = responsibleList.stream().filter(r -> r.getUser() == null).toList();
                return new ResponseEntity<>(withoutAccountResponsible, HttpStatus.OK);
            }
            // return new ResponseEntity<>(ResponsibleService.getResponsibleByPage(responsibleList, page, size), HttpStatus.OK);
            return new ResponseEntity<>(responsibleList, HttpStatus.OK);
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
            @RequestBody Responsible responsibleRequest) {
        try {
            responsibleService.updateResponsibleById(id, responsibleRequest.getDepartment().getId(), responsibleRequest);
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
