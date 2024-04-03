package com.vlsu.inventory.service;

import com.vlsu.inventory.model.Category;
import com.vlsu.inventory.model.Subcategory;
import com.vlsu.inventory.repository.CategoryRepository;
import com.vlsu.inventory.repository.SubcategoryRepository;
import com.vlsu.inventory.util.exception.ResourceHasDependenciesException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubcategoryService {
    private final SubcategoryRepository subcategoryRepository;
    private final CategoryRepository categoryRepository;

    public SubcategoryService(SubcategoryRepository subcategoryRepository, CategoryRepository categoryRepository, CategoryRepository categoryRepository1) {
        this.subcategoryRepository = subcategoryRepository;
        this.categoryRepository = categoryRepository1;
    }

    public List<Subcategory> getAllSubcategories() {
        return subcategoryRepository.findAll();
    }
    public Subcategory getSubcategoryById(Long id) throws ResourceNotFoundException {
        return subcategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subcategory with id: " + id + " not found"));
    }

    public List<Subcategory> getSubcategoriesByCategoryId(Long categoryId)
            throws ResourceNotFoundException {
        if (!categoryRepository.existsById(categoryId))
            throw new ResourceNotFoundException("Category with id '" + categoryId + "' not found");
        return subcategoryRepository.findByCategoryId(categoryId);
    }

    public void createSubcategory(Subcategory subcategoryRequest, Long categoryId)
            throws ResourceNotFoundException {
        Subcategory subcategory = categoryRepository.findById(categoryId).map(category -> {
            subcategoryRequest.setCategory(category);
            return subcategoryRepository.save(subcategoryRequest);
        }).orElseThrow(() -> new ResourceNotFoundException("Category with id '" + categoryId + "' not found"));
    }

    public void updateSubcategoryById(Long id, Subcategory subcategoryRequest, Long categoryId)
            throws ResourceNotFoundException {
        Category category = categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category with id '" + categoryId + "' not found"));
        Subcategory subcategory = subcategoryRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Subcategory with id '" + id + "' not found"));
        subcategory.setName(subcategoryRequest.getName());
        subcategory.setCategory(category);
        subcategoryRepository.save(subcategory);
    }

    public void deleteSubcategoryById(Long id) throws ResourceNotFoundException, ResourceHasDependenciesException {
        Subcategory subcategoryToDelete = subcategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategory with id: " + id + " not found"));
        if (!subcategoryToDelete.getEquipment().isEmpty()) {
            throw new ResourceHasDependenciesException("Category with id: " + id + " has relations with equipment");
        }
        subcategoryRepository.deleteById(id);
    }
}
