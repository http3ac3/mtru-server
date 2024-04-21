package com.vlsu.inventory.service;

import com.vlsu.inventory.dto.model.SubcategoryDto;
import com.vlsu.inventory.model.Category;
import com.vlsu.inventory.model.Subcategory;
import com.vlsu.inventory.repository.SubcategoryRepository;
import com.vlsu.inventory.util.exception.ResourceHasDependenciesException;
import com.vlsu.inventory.util.exception.ResourceNotFoundException;
import com.vlsu.inventory.util.mapping.SubcategoryMappingUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class SubcategoryService {

    SubcategoryRepository subcategoryRepository;
    CategoryService categoryService;

    public List<SubcategoryDto.Response.Default> getAll() {
        List<Subcategory> subcategories = subcategoryRepository.findAll();
        return subcategories.stream().map(SubcategoryMappingUtils::toDto).collect(Collectors.toList());
    }
    public SubcategoryDto.Response.Default getById(Long id) throws ResourceNotFoundException {
        Subcategory subcategory = subcategoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Subcategory with id: " + id + " not found"));
        return SubcategoryMappingUtils.toDto(subcategory);
    }

    public List<Subcategory> getByCategoryId(Long categoryId) throws ResourceNotFoundException {
        categoryService.getCategoryById(categoryId);
        return subcategoryRepository.findByCategoryId(categoryId);
    }

    public Subcategory create(SubcategoryDto.Request.Create request) throws ResourceNotFoundException {
        categoryService.getCategoryById(request.getCategory().getId());
        Subcategory subcategory = SubcategoryMappingUtils.fromDto(request);
        return subcategoryRepository.save(subcategory);
    }

    public Subcategory update(SubcategoryDto.Request.Update request) throws ResourceNotFoundException {
        Category category = categoryService.getCategoryById(request.getCategory().getId());

        Subcategory subcategory = subcategoryRepository
                .findById(request.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Subcategory with id '"
                        + request.getId() + "' not found"));

        subcategory.setName(request.getName());
        subcategory.setCategory(category);
        return subcategoryRepository.save(subcategory);
    }

    public void delete(Long id) throws ResourceNotFoundException, ResourceHasDependenciesException {
        Subcategory subcategory = subcategoryRepository
                .findCountOfEquipmentById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subcategory with id '" + id + "' not found"));
        if (!subcategory.getEquipment().isEmpty()) {
            throw new ResourceHasDependenciesException("Subcategory with id '" + id + "' has dependencies with equipment");
        }
        subcategoryRepository.deleteById(id);
    }
}
