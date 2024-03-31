package com.vlsu.inventory.service;

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

    public SubcategoryService(SubcategoryRepository subcategoryRepository, CategoryRepository categoryRepository) {
        this.subcategoryRepository = subcategoryRepository;
    }

    public List<Subcategory> getAllSubcategories() {
        return subcategoryRepository.findAll();
    }

    public void createSubcategory(Subcategory subcategory) {
        subcategoryRepository.save(subcategory);
    }

    public void updateSubcategoryById(Long id, Subcategory subcategory) throws ResourceNotFoundException {
        Subcategory subcategoryToUpdate = subcategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategory with id: " + id + " not found"));
        subcategoryToUpdate.setName(subcategory.getName());
        subcategoryToUpdate.setCategory(subcategory.getCategory());
        subcategoryRepository.save(subcategoryToUpdate);
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
