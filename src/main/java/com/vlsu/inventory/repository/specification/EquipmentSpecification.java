package com.vlsu.inventory.repository.specification;

import com.vlsu.inventory.model.Equipment;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.time.LocalDate;

public class EquipmentSpecification {
    public static Specification<Equipment> inventoryNumberStartsWith(String inventoryNumber) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder
                .like(equipment.get("inventoryNumber"), inventoryNumber + "%");
    }
    public static Specification<Equipment> nameStartsWith(String name) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder.like(equipment.get("name"), name + "%");
    }

    public static Specification<Equipment> initialCostFrom(BigDecimal initialCost) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(equipment.get("initialCost"), initialCost);
    }

    public static Specification<Equipment> initialCostTo(BigDecimal initialCost) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(equipment.get("initialCost"), initialCost);
    }


    public static Specification<Equipment> commissioningDateFrom(LocalDate from) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(equipment.get("commissioningDate"), from);
    }

    public static Specification<Equipment> commissioningDateTo(LocalDate to) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder
                .lessThanOrEqualTo(equipment.get("commissioningDate"), to);
    }

    public static Specification<Equipment> decommissioningDateFrom(LocalDate from) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(equipment.get("decommissioningDate"), from);
    }

    public static Specification<Equipment> decommissioningDateTo(LocalDate to) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder
                .lessThanOrEqualTo(equipment.get("decommissioningDate"), to);
    }

    public static Specification<Equipment> commissioningActNumberLike(String act) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder
                .like(equipment.get("commissioningActNumber"), act + "%");
    }

    public static Specification<Equipment> decommissioningActNumberLike(String act) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder
                .like(equipment.get("decommissioningActNumber"), act + "%");
    }

    public static Specification<Equipment> subcategoryIdEquals(Long subcategoryId) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder
                .equal(equipment.get("subcategory").get("id"), subcategoryId);
    }

    public static Specification<Equipment> responsibleIdEquals(Long responsibleId) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder
                .equal(equipment.get("responsible").get("id"), responsibleId);
    }

    public static Specification<Equipment> placementIdEquals(Long placementId) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder
                .equal(equipment.get("placement").get("id"), placementId);
    }
}
