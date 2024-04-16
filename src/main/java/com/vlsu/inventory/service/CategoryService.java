package com.vlsu.inventory.service;

import com.vlsu.inventory.model.Category;
import com.vlsu.inventory.repository.CategoryRepository;
import com.vlsu.inventory.util.exception.ResourceHasDependenciesException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import java.util.List;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CategoryService {

    CategoryRepository categoryRepository;

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    public Category getCategoryById(Long id) throws ResourceNotFoundException {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id: " + id + " not found"));
    }

    public Category getCategoryByName(String name) throws ResourceNotFoundException {
        return categoryRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException("Category with name: " + name + " not found"));
    }

    public void createCategory(Category category) {
        categoryRepository.save(category);
    }

    public void updateCategoryById(Long id, Category category) throws ResourceNotFoundException {
        Category categoryToUpdate = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id: " + id + " not found"));

        categoryToUpdate.setName(category.getName());
        categoryRepository.save(categoryToUpdate);
    }

    public void deleteCategoryById(Long id) throws ResourceNotFoundException, ResourceHasDependenciesException {
        Category categoryToDelete = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id: " + id + " not found"));

        if (!categoryToDelete.getSubcategories().isEmpty()) {
            throw new ResourceHasDependenciesException("Category with id: " + id + " has relations with subcategories");
        }
        categoryRepository.deleteById(id);
    }
}
