package com.vlsu.inventory.controller;

import com.vlsu.inventory.dto.model.DepartmentDto;
import com.vlsu.inventory.model.Department;
import com.vlsu.inventory.service.DepartmentService;
import com.vlsu.inventory.util.exception.ResourceHasDependenciesException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("api/v1")
public class DepartmentController {
    DepartmentService departmentService;

    @GetMapping("/departments")
    public ResponseEntity<List<DepartmentDto.Response.Default>> getAll() {
        return new ResponseEntity<>(departmentService.getAll(), HttpStatus.OK);
    }

    @GetMapping("/departments/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            DepartmentDto.Response.Default department = departmentService.getById(id);
            return new ResponseEntity<>(department, HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/departments")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> create(@RequestBody DepartmentDto.Request.Create department) {
        try {
            departmentService.create(department);
            return new ResponseEntity<>(department, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/departments")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> update(@RequestBody DepartmentDto.Request.Update departmentRequest) {
        try {
            departmentService.update(departmentRequest);
            return new ResponseEntity<>("Данные были успешно обновлены", HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/departments/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            departmentService.delete(id);
            return new ResponseEntity<>( HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ResourceHasDependenciesException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
