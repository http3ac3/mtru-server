package com.vlsu.inventory.repository;

import com.vlsu.inventory.model.Rent;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDateTime;
import java.util.List;

public interface RentRepository extends JpaRepository<Rent, Long>, JpaSpecificationExecutor<Rent> {
    static Specification<Rent> endDateIsNull() {
        return (rent, query, criteriaBuilder) -> criteriaBuilder.isNull(rent.get("endDate"));
    }
    static Specification<Rent> endDateIsNotNull() {
        return (rent, query, criteriaBuilder) -> criteriaBuilder.isNotNull(rent.get("endDate"));
    }
    static Specification<Rent> createDateTimeFrom(LocalDateTime dateTime) {
        return (rent, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(rent.get("createDate"), dateTime);
    }
    static Specification<Rent> createDateTimeTo(LocalDateTime dateTime) {
        return (rent, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(rent.get("createDate"), dateTime);
    }

    static Specification<Rent> endDateTimeFrom(LocalDateTime dateTime) {
        return (rent, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(rent.get("endDate"), dateTime);
    }

    static Specification<Rent> endDateTimeTo(LocalDateTime dateTime) {
        return (rent, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(rent.get("endDate"), dateTime);
    }
    List<Rent> findByEquipmentId(Long equipmentId);
    List<Rent> findByResponsibleId(Long responsibleId);
    List<Rent> findByPlacementId(Long placementId);
    void deleteByEquipmentId(Long equipmentId);
}
