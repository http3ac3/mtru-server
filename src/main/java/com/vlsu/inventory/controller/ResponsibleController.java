package com.vlsu.inventory.controller;

import com.vlsu.inventory.dto.model.ResponsibleDto;
import com.vlsu.inventory.model.Responsible;
import com.vlsu.inventory.model.User;
import com.vlsu.inventory.service.ResponsibleService;
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

import java.util.*;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("api/v1")
public class ResponsibleController {

    ResponsibleService responsibleService;

    // TODO Do fetching responsible without accounts with Responsible DTO usage
    @GetMapping("/responsible")
    public ResponseEntity<?> getAll(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String lastName,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) Boolean isFinanciallyResponsible,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Boolean hasAccount) {
        try {
            List<ResponsibleDto.Response.Default> responsibleList =
                    responsibleService.getAll(firstName, lastName, isFinanciallyResponsible, departmentId);
            return ResponseEntity.ok(responsibleList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/responsible/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(responsibleService.getById(id));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/responsible/current-user")
    public ResponseEntity<?> getByPrincipal(@AuthenticationPrincipal User principal) {
        return ResponseEntity.ok(responsibleService.getByPrincipal(principal));
    }

    @PostMapping("/responsible")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> create(@RequestBody ResponsibleDto.Request.Create request) {
        try {
            Responsible created = responsibleService.create(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/responsible")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> update(@RequestBody ResponsibleDto.Request.Update request) {
        System.out.println(request.isFinanciallyResponsible());
        try {
            return ResponseEntity.ok(responsibleService.update(request));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/responsible/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        try {
            responsibleService.delete(id);
            return ResponseEntity.ok("Данные были успешно удалены");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (ResourceHasDependenciesException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
