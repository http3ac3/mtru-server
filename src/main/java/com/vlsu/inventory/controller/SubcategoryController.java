package com.vlsu.inventory.controller;

import com.vlsu.inventory.model.Subcategory;
import com.vlsu.inventory.service.SubcategoryService;
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
public class SubcategoryController {

    SubcategoryService subcategoryService;

    @GetMapping("/subcategories/all")
    public ResponseEntity<List<Subcategory>> getAllSubcategories() {
        return new ResponseEntity<>(subcategoryService.getAllSubcategories(), HttpStatus.OK);
    }

    @GetMapping("/categories/{categoryId}/subcategories")
    public ResponseEntity<?> getSubcategoriesByCategoryId(
            @PathVariable Long categoryId) {
        try {
            return new ResponseEntity<>(subcategoryService.getSubcategoriesByCategoryId(categoryId), HttpStatus.OK);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/subcategories/{id}")
    public ResponseEntity<?> getSubcategoryById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>(subcategoryService.getSubcategoryById(id), HttpStatus.OK);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/categories/{categoryId}/subcategories")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<?> createSubcategory(
            @PathVariable Long categoryId, @RequestBody Subcategory subcategory) {
        try {
            subcategoryService.createSubcategory(subcategory, categoryId);
            return new ResponseEntity<>(subcategory, HttpStatus.CREATED);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/subcategories/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> updateSubcategoryByCategoryId(
            @RequestParam Long categoryId, @PathVariable Long id, @RequestBody Subcategory subcategoryRequest) {
        try {
            subcategoryService.updateSubcategoryById(id, subcategoryRequest, categoryId);
            return new ResponseEntity<>("Данные были успешно обновлены", HttpStatus.OK);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/subcategories/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<String> deleteSubcategoryById(@PathVariable Long id) {
        try {
            subcategoryService.deleteSubcategoryById(id);
            return new ResponseEntity<>("Данные были успешно удалены", HttpStatus.OK);
        } catch (ResourceNotFoundException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
        } catch (ResourceHasDependenciesException exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
