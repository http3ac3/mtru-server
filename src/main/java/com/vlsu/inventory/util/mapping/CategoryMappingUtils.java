package com.vlsu.inventory.util.mapping;

import com.vlsu.inventory.dto.model.CategoryDto;
import com.vlsu.inventory.model.Category;

public class CategoryMappingUtils {

    /**
     * Converts Category entity from create DTO request
     *
     * @param dto create Category DTO request
     * @return Category entity object
     */
    public static Category fromDto(CategoryDto.Request.Create dto) {
        Category category = new Category();
        category.setName(dto.getName());
        return category;
    }

    /**
     * Converts Category entity object from response DTO object
     *
     * @param dto Response DTO object
     * @return Category entity object
     */
    public static Category fromDto(CategoryDto.Response.Default dto) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        return category;
    }

    /**
     * Converts Category DTO update request object to Category entity object
     *
     * @param dto Category DTO update request object
     * @return Category entity object
     */
    public static Category fromDto(CategoryDto.Request.Update dto) {
        Category category = new Category();
        category.setId(dto.getId());
        category.setName(dto.getName());
        return category;
    }

    /**
     * Converts Category entity object to Category DTO object
     *
     * @param category Category entity object
     * @return Category DTO object
     */
    public static CategoryDto.Response.Default toDto(Category category) {
        return new CategoryDto.Response.Default(category.getId(), category.getName());
    }
}
