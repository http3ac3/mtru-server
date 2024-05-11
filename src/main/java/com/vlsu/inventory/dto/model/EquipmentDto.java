package com.vlsu.inventory.dto.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

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
    interface Responsible { ResponsibleDto.Response.Default getResponsible(); }
    interface Subcategory { SubcategoryDto.Response.WithoutCategory getSubcategory(); }
    interface Placement { PlacementDto.Response.Default getPlacement(); }
    interface CurrentRent { RentDto.Response.Default getCurrentRent(); }
    interface ImageFile { MultipartFile getImage(); }
    interface ResponsibleId { Long getResponsibleId(); }
    interface PlacementId { Long getPlacementId(); }
    interface SubcategoryId { Long getSubcategoryId();}

        public enum Request {;
        @Getter @Builder
        public static class Create implements
                InventoryNumber, Name, InitialCost,
                CommissioningDate, CommissioningActNumber, Description, ImageFile,
                ResponsibleId, PlacementId, SubcategoryId {
            String inventoryNumber;
            String name;
            BigDecimal initialCost;
            LocalDate commissioningDate;
            String commissioningActNumber;
            String description;
            MultipartFile image;
//            ResponsibleDto.Response.WithoutDepartment responsible;
//            SubcategoryDto.Response.WithoutCategory subcategory;
//            PlacementDto.Response.Default placement;
            Long responsibleId;
            Long placementId;
            Long subcategoryId;
        }

        @Getter @Builder
        public static class Update implements
                Id, InventoryNumber, Name, CommissioningDate, CommissioningActNumber,
                Description, ImageFile, DecommissioningDate, DecommissioningActNumber,
                ResponsibleId, PlacementId, SubcategoryId {
            Long id;
            String inventoryNumber;
            String name;
            BigDecimal initialCost;
            LocalDate commissioningDate;
            String commissioningActNumber;
            LocalDate decommissioningDate;
            String decommissioningActNumber;
            String description;
            MultipartFile image;
//            ResponsibleDto.Response.WithoutDepartment responsible;
//            SubcategoryDto.Response.WithoutCategory subcategory;
//            PlacementDto.Response.Default placement;
            Long responsibleId;
            Long placementId;
            Long subcategoryId;
        }
    }

    public enum Response {;
        @Getter @Builder
        public static class Default implements
                Id, InventoryNumber, Name, CommissioningDate, CommissioningActNumber,
                Description, ImageData, DecommissioningDate, DecommissioningActNumber,
                Responsible, Subcategory, Placement, CurrentRent {
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
            ResponsibleDto.Response.Default responsible;
            SubcategoryDto.Response.WithoutCategory subcategory;
            PlacementDto.Response.Default placement;
            RentDto.Response.Default currentRent;
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
