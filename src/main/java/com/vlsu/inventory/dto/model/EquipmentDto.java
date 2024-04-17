package com.vlsu.inventory.dto.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vlsu.inventory.model.Placement;
import com.vlsu.inventory.model.Responsible;
import com.vlsu.inventory.model.Subcategory;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDate;


@FieldDefaults(level = AccessLevel.PRIVATE)
public enum EquipmentDto {;
    interface Id {  Long getId(); }
    interface InventoryNumber { String getInventoryNumber(); }
    interface Name {  String getName(); }
    interface InitialCost {  BigDecimal getInitialCost(); }
    interface CommissioningDate { LocalDate getCommissioningDate(); }
    interface CommissioningActNumber { String getCommissioningActNumber(); }
    interface DecommissioningDate { LocalDate getDecommissioningDate(); }
    interface DecommissioningActNumber { String getDecommissioningActNumber(); }
    interface ImageData { String getImageData(); }
    interface Description { String getDescription(); }
    interface Responsible { ResponsibleDto.Response.WithoutDepartment getResponsible(); }
    interface Subcategory { SubcategoryDto.Response.WithoutCategory getSubcategory(); }
    interface Placement { PlacementDto.Response.Default getPlacement(); }

    public enum Request {;
        @Getter @Builder
        public static class Create implements
                InventoryNumber, Name, InitialCost,
                CommissioningDate, CommissioningActNumber, Description, ImageData,
                Responsible, Subcategory, Placement {
            String inventoryNumber;
            String name;
            BigDecimal initialCost;
            LocalDate commissioningDate;
            String commissioningActNumber;
            String description;
            String imageData;
            ResponsibleDto.Response.WithoutDepartment responsible;
            SubcategoryDto.Response.WithoutCategory subcategory;
            PlacementDto.Response.Default placement;
        }

        @Getter @Builder
        public static class Update implements
                Id, InventoryNumber, Name, CommissioningDate, CommissioningActNumber,
                Description, ImageData, DecommissioningDate, DecommissioningActNumber,
                Responsible, Subcategory, Placement {
            Long id;
            String inventoryNumber;
            String name;
            BigDecimal initialCost;
            LocalDate commissioningDate;
            String commissioningActNumber;
            LocalDate decommissioningDate;
            String decommissioningActNumber;
            String description;
            String imageData;
            ResponsibleDto.Response.WithoutDepartment responsible;
            SubcategoryDto.Response.WithoutCategory subcategory;
            PlacementDto.Response.Default placement;
        }
    }

    public enum Response {;
        @Getter @Builder
        public static class Default implements
                Id, InventoryNumber, Name, CommissioningDate, CommissioningActNumber,
                Description, ImageData, DecommissioningDate, DecommissioningActNumber,
                Responsible, Subcategory, Placement {
            Long id;
            String inventoryNumber;
            String name;
            BigDecimal initialCost;
            LocalDate commissioningDate;
            String commissioningActNumber;
            LocalDate decommissioningDate;
            String decommissioningActNumber;
            String description;
            String imageData;
            ResponsibleDto.Response.WithoutDepartment responsible;
            SubcategoryDto.Response.WithoutCategory subcategory;
            PlacementDto.Response.Default placement;
        }

        @Getter @Builder
        public static class WithoutAssociations implements
                Id, InventoryNumber, Name, CommissioningDate, CommissioningActNumber,
                Description, ImageData, DecommissioningDate, DecommissioningActNumber {
            Long id;
            String inventoryNumber;
            String name;
            BigDecimal initialCost;
            LocalDate commissioningDate;
            String commissioningActNumber;
            LocalDate decommissioningDate;
            String decommissioningActNumber;
            String description;
            String imageData;
        }

        @Getter @AllArgsConstructor
        public static class Public implements Id, InventoryNumber, Name {
            Long id;
            String inventoryNumber;
            String name;
        }
    }
}
