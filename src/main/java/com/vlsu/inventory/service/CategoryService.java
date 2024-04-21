package com.vlsu.inventory.service;

import com.vlsu.inventory.dto.model.CategoryDto;
import com.vlsu.inventory.model.Category;
import com.vlsu.inventory.repository.CategoryRepository;
import com.vlsu.inventory.util.exception.ResourceHasDependenciesException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import com.vlsu.inventory.util.mapping.CategoryMappingUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class CategoryService {

    CategoryRepository categoryRepository;

    public List<CategoryDto.Response.Default> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(CategoryMappingUtils::toDto).collect(Collectors.toList());
    }

    public Category getCategoryById(Long id) throws ResourceNotFoundException {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id: " + id + " not found"));
    }

    public void create(CategoryDto.Request.Create request) {
        Category category = CategoryMappingUtils.fromDto(request);
        categoryRepository.save(category);
    }

    public void update(CategoryDto.Request.Update request) throws ResourceNotFoundException {
        Category category = categoryRepository.findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Category with id: " + request.getId() + " not found"));

        category.setName(request.getName());
        categoryRepository.save(category);
    }

    public void delete(Long id) throws ResourceNotFoundException, ResourceHasDependenciesException {
        Optional<Category> category = categoryRepository.findWithSubcategoriesById(id);

        if (category.isEmpty()) {
            throw new ResourceNotFoundException("Category with id '" + id + "' not found");
        }

        if (!category.get().getSubcategories().isEmpty()) {
            throw new ResourceHasDependenciesException("Category with id: " + id + " has relations with subcategories");
        }
        categoryRepository.deleteById(id);
    }
}
