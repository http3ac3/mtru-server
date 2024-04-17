package com.vlsu.inventory.util.mapping;

import com.vlsu.inventory.dto.model.DepartmentDto;
import com.vlsu.inventory.model.Department;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PUBLIC)
public class DepartmentMappingUtils {

    /**
     * Converts Department DTO create request object to Department entity object
     *
     * @param dto Department DTO create request
     * @return Department entity
     */
    static Department fromDto(DepartmentDto.Request.Create dto) {
        Department department = new Department();
        department.setName(dto.getName());
        return department;
    }

    /**
     * Converts Department DTO update request object to Department entity object
     *
     * @param dto Department DTO update request
     * @return Department entity
     */
    static Department fromDto(DepartmentDto.Request.Update dto) {
        Department department = new Department();
        department.setId(dto.getId());
        department.setName(dto.getName());
        return department;
    }

    /**
     * Converts Department DTO response object to Department entity object
     *
     * @param dto Department DTO response object
     * @return Department entity
     */
    static Department fromDto(DepartmentDto.Response.Default dto) {
        Department department = new Department();
        department.setId(dto.getId());
        department.setName(dto.getName());
        return department;
    }

    /**
     * Converts Department entity object to Department DTO response object
     *
     * @param department Department entity
     * @return Department DTO response
     */
    static DepartmentDto.Response.Default toDto(Department department) {
        return new DepartmentDto.Response.Default(department.getId(), department.getName());
    }
}
