package com.vlsu.inventory.repository;

import com.vlsu.inventory.model.Equipment;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface EquipmentRepository extends JpaRepository<Equipment, Long>, JpaSpecificationExecutor<Equipment> {
    List<Equipment> findByInventoryNumberStartingWith(String inventoryNumber);
    static Specification<Equipment> inventoryNumberStartsWith(String inventoryNumber) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder
                .like(equipment.get("inventoryNumber"), inventoryNumber + "%");
    }

    static Specification<Equipment> nameStartsWith(String name) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder.like(equipment.get("name"), name + "%");
    }

    static Specification<Equipment> initialCostFrom(BigDecimal initialCost) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(equipment.get("initialCost"), initialCost);
    }

    static Specification<Equipment> initialCostTo(BigDecimal initialCost) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(equipment.get("initialCost"), initialCost);
    }


    static Specification<Equipment> commissioningDateFrom(LocalDate from) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(equipment.get("commissioningDate"), from);
    }

    static Specification<Equipment> commissioningDateTo(LocalDate to) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder
                .lessThanOrEqualTo(equipment.get("commissioningDate"), to);
    }

    static Specification<Equipment> decommissioningDateFrom(LocalDate from) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder
                .greaterThanOrEqualTo(equipment.get("decommissioningDate"), from);
    }

    static Specification<Equipment> decommissioningDateTo(LocalDate to) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder
                .lessThanOrEqualTo(equipment.get("decommissioningDate"), to);
    }

    static Specification<Equipment> commissioningActNumberLike(String act) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder
                .like(equipment.get("commissioningActNumber"), act + "%");
    }

    static Specification<Equipment> decommissioningActNumberLike(String act) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder
                .like(equipment.get("decommissioningActNumber"), act + "%");
    }

    static Specification<Equipment> subcategoryIdEquals(Long subcategoryId) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder
                .equal(equipment.get("subcategory").get("id"), subcategoryId);
    }

    static Specification<Equipment> responsibleIdEquals(Long responsibleId) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder
                .equal(equipment.get("responsible").get("id"), responsibleId);
    }

    static Specification<Equipment> placementIdEquals(Long placementId) {
        return (equipment, query, criteriaBuilder) -> criteriaBuilder
                .equal(equipment.get("placement").get("id"), placementId);
    }
    List<Equipment> findByResponsibleId(Long responsibleId);
    List<Equipment> findByPlacementId(Long placementId);
    List<Equipment> findBySubcategoryId(Long subcategoryId);
}
