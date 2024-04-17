package com.vlsu.inventory.util.mapping;

import com.vlsu.inventory.dto.model.EquipmentDto;
import com.vlsu.inventory.model.Equipment;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PUBLIC)
public class EquipmentMappingUtils {
    static Equipment fromDto(EquipmentDto.Request.Create dto) {
        return Equipment.builder()
                .inventoryNumber(dto.getInventoryNumber())
                .name(dto.getName())
                .initialCost(dto.getInitialCost())
                .commissioningDate(dto.getCommissioningDate())
                .commissioningActNumber(dto.getCommissioningActNumber())
                .description(dto.getDescription())
                .imageData(dto.getImageData())
                .responsible(ResponsibleMappingUtils.fromDto(dto.getResponsible()))
                .placement(PlacementMappingUtils.fromDto(dto.getPlacement()))
                .subcategory(SubcategoryMappingUtils.fromDto(dto.getSubcategory()))
                .build();
    }

    /**
     * Converts Equipment DTO update request object to Equipment entity object
     *
     * @param dto Equipment DTO update request
     * @return Equipment entity
     */
    static Equipment fromDto(EquipmentDto.Request.Update dto) {
        return Equipment.builder()
                .id(dto.getId())
                .inventoryNumber(dto.getInventoryNumber())
                .name(dto.getName())
                .initialCost(dto.getInitialCost())
                .commissioningDate(dto.getCommissioningDate())
                .commissioningActNumber(dto.getCommissioningActNumber())
                .decommissioningDate(dto.getDecommissioningDate())
                .decommissioningActNumber(dto.getDecommissioningActNumber())
                .description(dto.getDescription())
                .imageData(dto.getImageData())
                .responsible(ResponsibleMappingUtils.fromDto(dto.getResponsible()))
                .placement(PlacementMappingUtils.fromDto(dto.getPlacement()))
                .subcategory(SubcategoryMappingUtils.fromDto(dto.getSubcategory()))
                .build();
    }

    /**
     * Converts Equipment DTO response object to Equipment entity object
     *
     * @param dto Equipment DTO response
     * @return Equipment entity
     */
    static Equipment fromDto(EquipmentDto.Response.Default dto) {
        return Equipment.builder()
                .id(dto.getId())
                .inventoryNumber(dto.getInventoryNumber())
                .name(dto.getName())
                .initialCost(dto.getInitialCost())
                .commissioningDate(dto.getCommissioningDate())
                .commissioningActNumber(dto.getCommissioningActNumber())
                .decommissioningDate(dto.getDecommissioningDate())
                .decommissioningActNumber(dto.getDecommissioningActNumber())
                .description(dto.getDescription())
                .imageData(dto.getImageData())
                .responsible(ResponsibleMappingUtils.fromDto(dto.getResponsible()))
                .placement(PlacementMappingUtils.fromDto(dto.getPlacement()))
                .subcategory(SubcategoryMappingUtils.fromDto(dto.getSubcategory()))
                .build();
    }

    /**
     * Converts Equipment DTO response object without any associations with Responsible, Subcategory and Placement entities to
     * Equipment entity object
     *
     * @param dto Equipment DTO response without associations
     * @return Equipment entity object
     */
    static Equipment fromDto(EquipmentDto.Response.WithoutAssociations dto) {
        return Equipment.builder()
                .id(dto.getId())
                .inventoryNumber(dto.getInventoryNumber())
                .name(dto.getName())
                .initialCost(dto.getInitialCost())
                .commissioningDate(dto.getCommissioningDate())
                .commissioningActNumber(dto.getCommissioningActNumber())
                .decommissioningDate(dto.getDecommissioningDate())
                .decommissioningActNumber(dto.getDecommissioningActNumber())
                .description(dto.getDescription())
                .imageData(dto.getImageData())
                .build();
    }

    static Equipment fromDto(EquipmentDto.Response.Public dto) {
        return Equipment.builder()
                .id(dto.getId())
                .name(dto.getName())
                .inventoryNumber(dto.getInventoryNumber())
                .build();
    }

    /**
     * Converts Equipment entity object to Equipment DTO response default object
     *
     * @param equipment Equipment entity
     * @return Equipment DTO response
     */
    static EquipmentDto.Response.Default toDto(Equipment equipment) {
        return EquipmentDto.Response.Default.builder()
                .id(equipment.getId())
                .inventoryNumber(equipment.getInventoryNumber())
                .name(equipment.getName())
                .initialCost(equipment.getInitialCost())
                .commissioningDate(equipment.getCommissioningDate())
                .commissioningActNumber(equipment.getCommissioningActNumber())
                .decommissioningDate(equipment.getDecommissioningDate())
                .decommissioningActNumber(equipment.getDecommissioningActNumber())
                .description(equipment.getDescription())
                .imageData(equipment.getImageData())
                .responsible(ResponsibleMappingUtils.toDtoWithoutDepartment(equipment.getResponsible()))
                .placement(PlacementMappingUtils.toDto(equipment.getPlacement()))
                .subcategory(SubcategoryMappingUtils.toDtoWithoutCategory(equipment.getSubcategory()))
                .build();
    }

    /**
     * Converts Equipment entity object to Equipment DTO response object without any associations with
     * Subcategory, Responsible and Placement DTO objects
     *
     * @param equipment Equipment entity
     * @return Equipment DTO response without associations
     */
    static EquipmentDto.Response.WithoutAssociations toDtoWithoutAssociations(Equipment equipment) {
        return EquipmentDto.Response.WithoutAssociations.builder()
                .id(equipment.getId())
                .inventoryNumber(equipment.getInventoryNumber())
                .name(equipment.getName())
                .initialCost(equipment.getInitialCost())
                .commissioningDate(equipment.getCommissioningDate())
                .commissioningActNumber(equipment.getCommissioningActNumber())
                .decommissioningDate(equipment.getDecommissioningDate())
                .decommissioningActNumber(equipment.getDecommissioningActNumber())
                .description(equipment.getDescription())
                .imageData(equipment.getImageData())
                .build();
    }

    /**
     * Converts Equipment entity object to Equipment DTO object with public information (ID, inventory number, name)
     *
     * @param equipment Equipment entity
     * @return Equipment DTO response public
     */
    static EquipmentDto.Response.Public toDtoPublic(Equipment equipment) {
        return new EquipmentDto.Response.Public(equipment.getId(), equipment.getInventoryNumber(), equipment.getName());
    }

}
