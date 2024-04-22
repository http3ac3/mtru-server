package com.vlsu.inventory.dto.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@FieldDefaults(level = AccessLevel.PRIVATE)
public enum RentDto {;
    interface Id { Long getId(); }
    interface CreateDateTime { LocalDateTime getCreateDateTime(); }
    interface EndDateTime { LocalDateTime getEndDateTime(); }
    interface Description { String getDescription(); }
    interface Equipment { EquipmentDto.Response.Public getEquipment(); }
    interface Responsible { ResponsibleDto.Response.WithoutDepartment getResponsible(); }
    interface Placement { PlacementDto.Response.Default getPlacement(); }

    public enum Request {;

        @Getter @AllArgsConstructor
        public static class Create implements Description, Equipment, Placement {
            String description;
            EquipmentDto.Response.Public equipment;
            PlacementDto.Response.Default placement;
        }
    }

    public enum Response {;

        @Getter @AllArgsConstructor
        public static class Default implements
                Id, CreateDateTime, EndDateTime, Description,
                Equipment, Responsible, Placement {
            Long id;
            LocalDateTime createDateTime;
            LocalDateTime endDateTime;
            String description;
            EquipmentDto.Response.Public equipment;
            ResponsibleDto.Response.WithoutDepartment responsible;
            PlacementDto.Response.Default placement;
        }

        @Getter @AllArgsConstructor
        public static class UserUnclosed implements
                Id, CreateDateTime, Description,
                Equipment, Placement {
            Long id;
            LocalDateTime createDateTime;
            String description;
            EquipmentDto.Response.Public equipment;
            PlacementDto.Response.Default placement;
        }
    }

}
