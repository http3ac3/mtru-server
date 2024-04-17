package com.vlsu.inventory.util.mapping;

import com.vlsu.inventory.dto.model.SubcategoryDto;
import com.vlsu.inventory.model.Subcategory;
public class SubcategoryMappingUtils {

    /**
     * Converts Subcategory DTO create request to Subcategory entity object
     *
     * @param dto Subcategory DTO request
     * @return Subcategory entity object
     */
    public static Subcategory fromDto(SubcategoryDto.Request.Create dto) {
        Subcategory subcategory = new Subcategory();
        subcategory.setName(dto.getName());
        subcategory.setCategory(CategoryMappingUtils.fromDto(dto.getCategory()));
        return subcategory;
    }

    /**
     * Converts Subcategory DTO response object (standard Subcategory entity) to Subcategory entity object
     *
     * @param dto Subcategory DTO response
     * @return Subcategory entity object
     */
    public static Subcategory fromDto(SubcategoryDto.Response.Default dto) {
        Subcategory subcategory = new Subcategory();
        subcategory.setId(dto.getId());
        subcategory.setName(dto.getName());
        subcategory.setCategory(CategoryMappingUtils.fromDto(dto.getCategory()));
        return subcategory;
    }

    /**
     * Converts Subcategory DTO update request object to Subcategory entity object
     *
     * @param dto Subcategory DTO update request object
     * @return Subcategory entity object
     */
    public static Subcategory fromDto(SubcategoryDto.Request.Update dto) {
        Subcategory subcategory = new Subcategory();
        subcategory.setId(dto.getId());
        subcategory.setName(dto.getName());
        subcategory.setCategory(CategoryMappingUtils.fromDto(dto.getCategory()));
        return subcategory;
    }

    /**
     * Converts Subcategory DTO response object without Category association to Subcategory entity
     *
     * @param dto Subcategory DTO response without Category
     * @return Subcategory entity
     */
    public static Subcategory fromDto(SubcategoryDto.Response.WithoutCategory dto) {
        Subcategory subcategory = new Subcategory();
        subcategory.setId(dto.getId());
        subcategory.setName(dto.getName());
        return subcategory;
    }

    /**
     * Converts Subcategory entity object to Subcategory DTO response object
     *
     * @param subcategory Subcategory entity object
     * @return Subcategory DTO response object
     */
    public static SubcategoryDto.Response.Default toDto(Subcategory subcategory) {
        return new SubcategoryDto.Response.Default(
                subcategory.getId(),
                subcategory.getName(),
                CategoryMappingUtils.toDto(subcategory.getCategory()));
    }

    /**
     * Converts Subcategory entity object to Subcategory DTO response object without Category entity association
     *
     * @param subcategory Subcategory entity
     * @return Subcategory DTO response without Category
     */
    public static SubcategoryDto.Response.WithoutCategory toDtoWithoutCategory(Subcategory subcategory) {
        return new SubcategoryDto.Response.WithoutCategory(
                subcategory.getId(),
                subcategory.getName()
        );
    }


}
