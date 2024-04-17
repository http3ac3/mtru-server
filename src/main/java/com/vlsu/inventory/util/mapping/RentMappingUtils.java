package com.vlsu.inventory.util.mapping;

import com.vlsu.inventory.dto.model.RentDto;
import com.vlsu.inventory.model.Rent;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PUBLIC)
public class RentMappingUtils {

    /**
     * Converts Rent DTO create request object to Rent entity object
     *
     * @param dto Rent DTO create request
     * @return Rent entity
     */
    static Rent fromDto(RentDto.Request.Create dto) {
        Rent rent = new Rent();
        rent.setDescription(dto.getDescription());
        rent.setEquipment(EquipmentMappingUtils.fromDto(dto.getEquipment()));
        rent.setResponsible(ResponsibleMappingUtils.fromDto(dto.getResponsible()));
        rent.setPlacement(PlacementMappingUtils.fromDto(dto.getPlacement()));
        return rent;
    }

    /**
     * Converts Rent entity object to Rent DTO default response object
     * @param rent Rent entity
     * @return Rent DTO response
     */
    static RentDto.Response.Default toDto(Rent rent) {
        return new RentDto.Response.Default(
                rent.getId(),
                rent.getCreateDateTime(),
                rent.getEndDateTime(),
                rent.getDescription(),
                EquipmentMappingUtils.toDtoPublic(rent.getEquipment()),
                ResponsibleMappingUtils.toDtoWithoutDepartment(rent.getResponsible()),
                PlacementMappingUtils.toDto(rent.getPlacement())
        );
    }

    /**
     * Converts Rent entity object to Rent DTO response object that shows User's unclosed rents
     *
     * @param rent Rent entity
     * @return Rent DTO User's unclosed rents response
     */
    static RentDto.Response.UserUnclosed toDtoUserUnclosed(Rent rent) {
        return new RentDto.Response.UserUnclosed(
                rent.getId(),
                rent.getCreateDateTime(),
                rent.getDescription(),
                EquipmentMappingUtils.toDtoPublic(rent.getEquipment()),
                PlacementMappingUtils.toDto(rent.getPlacement())
        );
    }
}
