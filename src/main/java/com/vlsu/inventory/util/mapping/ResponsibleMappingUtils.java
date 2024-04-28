package com.vlsu.inventory.util.mapping;

import com.vlsu.inventory.dto.model.ResponsibleDto;
import com.vlsu.inventory.model.Responsible;

public class ResponsibleMappingUtils {

    /**
     * Converts Responsible DTO create request to Responsible entity object
     *
     * @param dto Responsible DTO create request
     * @return Responsible entity
     */
    public static Responsible fromDto(ResponsibleDto.Request.Create dto) {
        Responsible responsible =  new Responsible(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getPatronymic(),
                dto.getPosition(),
                dto.getPhoneNumber(),
                dto.isFinanciallyResponsible()
        );
        responsible.setDepartment(DepartmentMappingUtils.fromDto(dto.getDepartment()));
        return responsible;
    }

    /**
     * Converts Responsible DTO update request object to Responsible entity object
     *
     * @param dto Responsible DTO update request
     * @return Responsible entity object
     */
    public static Responsible fromDto(ResponsibleDto.Request.Update dto) {
        Responsible responsible =  new Responsible(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getPatronymic(),
                dto.getPosition(),
                dto.getPhoneNumber(),
                dto.isFinanciallyResponsible()
        );
        responsible.setId(dto.getId());
        responsible.setDepartment(DepartmentMappingUtils.fromDto(dto.getDepartment()));
        return responsible;
    }

    /**
     * Converts Responsible DTO response object to Responsible entity object
     *
     * @param dto Responsible DTO response
     * @return Responsible entity
     */
    public static Responsible fromDto(ResponsibleDto.Response.Default dto) {
        Responsible responsible =  new Responsible(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getPatronymic(),
                dto.getPosition(),
                dto.getPhoneNumber(),
                dto.isFinanciallyResponsible()
        );
        responsible.setId(dto.getId());
        responsible.setDepartment(DepartmentMappingUtils.fromDto(dto.getDepartment()));
        return responsible;
    }

    /**
     * Converts Responsible DTO response object without Department association to Responsible entity object
     *
     * @param dto Responsible DTO response without Department
     * @return Responsible entity
     */
    public static Responsible fromDto(ResponsibleDto.Response.WithoutDepartment dto) {
        Responsible responsible =  new Responsible(
                dto.getFirstName(),
                dto.getLastName(),
                dto.getPatronymic(),
                dto.getPosition(),
                dto.getPhoneNumber(),
                dto.isFinanciallyResponsible()
        );
        responsible.setId(dto.getId());
        return responsible;
    }

    /**
     * Converts Responsible entity object to Responsible DTO response object
     *
     * @param responsible Responsible entity
     * @return Responsible DTO response
     */
    public static ResponsibleDto.Response.Default toDto(Responsible responsible) {
        return ResponsibleDto.Response.Default.builder()
                .id(responsible.getId())
                .lastName(responsible.getLastName())
                .firstName(responsible.getFirstName())
                .patronymic(responsible.getPatronymic())
                .position(responsible.getPosition())
                .phoneNumber(responsible.getPhoneNumber())
                .isFinanciallyResponsible(responsible.isFinanciallyResponsible())
                .department(DepartmentMappingUtils.toDto(responsible.getDepartment()))
                .build();
    }

    /**
     * Converts Responsible entity object to Responsible DTO response object without Department entity association
     *
     * @param responsible Responsible entity
     * @return Responsible DTO without Department
     */
    public static ResponsibleDto.Response.WithoutDepartment toDtoWithoutDepartment(Responsible responsible) {
        return ResponsibleDto.Response.WithoutDepartment.builder()
                .id(responsible.getId())
                .lastName(responsible.getLastName())
                .firstName(responsible.getFirstName())
                .patronymic(responsible.getPatronymic())
                .position(responsible.getPosition())
                .phoneNumber(responsible.getPhoneNumber())
                .isFinanciallyResponsible(responsible.isFinanciallyResponsible())
                .build();
    }
}
