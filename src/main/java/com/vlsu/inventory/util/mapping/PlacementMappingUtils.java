package com.vlsu.inventory.util.mapping;

import com.vlsu.inventory.dto.model.PlacementDto;
import com.vlsu.inventory.model.Placement;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PUBLIC)
public class PlacementMappingUtils {

    /**
     * Converts Placement DTO create request entity object to Placement entity object
     *
     * @param dto Placement DTO create request
     * @return Placement entity
     */
    static Placement fromDto(PlacementDto.Request.Create dto) {
        Placement placement = new Placement();
        placement.setName(dto.getName());
        return placement;
    }

    /**
     * Converts Placement DTO update request object to Placement entity object
     *
     * @param dto Placement DTO update request
     * @return Placement entity
     */
    static Placement fromDto(PlacementDto.Request.Update dto) {
        Placement placement = new Placement();
        placement.setId(dto.getId());
        placement.setName(dto.getName());
        return placement;
    }

    /**
     * Convert Placement DTO response object to Placement entity object
     *
     * @param dto Placement DTO response
     * @return Placement entity
     */
    static Placement fromDto(PlacementDto.Response.Default dto) {
        Placement placement = new Placement();
        placement.setId(dto.getId());
        placement.setName(dto.getName());
        return placement;
    }

    /**
     * Converts Placement entity object to Placement DTO response object
     *
     * @param placement Placement entity
     * @return Placement DTO response
     */
    static PlacementDto.Response.Default toDto(Placement placement) {
        return new PlacementDto.Response.Default(placement.getId(), placement.getName());
    }
}
