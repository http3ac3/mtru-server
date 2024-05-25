package com.vlsu.inventory.repository.specification;

import com.vlsu.inventory.model.Rent;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;

public class RentSpecification {
    public static Specification<Rent> endDateIsNull() {
        return (rent, query, criteriaBuilder) -> criteriaBuilder.isNull(rent.get("endDateTime"));
    }
    public static Specification<Rent> endDateIsNotNull() {
        return (rent, query, criteriaBuilder) -> criteriaBuilder.isNotNull(rent.get("endDateTime"));
    }
    public static Specification<Rent> createDateTimeFrom(LocalDateTime dateTime) {
        return (rent, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(rent.get("createDateTime"), dateTime);
    }
    public static Specification<Rent> createDateTimeTo(LocalDateTime dateTime) {
        return (rent, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(rent.get("createDateTime"), dateTime);
    }

    public static Specification<Rent> endDateTimeFrom(LocalDateTime dateTime) {
        return (rent, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(rent.get("endDateTime"), dateTime);
    }

    public static Specification<Rent> endDateTimeTo(LocalDateTime dateTime) {
        return (rent, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(rent.get("endDateTime"), dateTime);
    }

    public static Specification<Rent> equipmentIdEquals(Long equipmentId) {
        return (rent, query, criteriaBuilder) -> criteriaBuilder.equal(rent.get("equipment").get("id"), equipmentId);
    }

    public static Specification<Rent> responsibleIdEquals(Long responsibleId) {
        return (rent, query, criteriaBuilder) -> criteriaBuilder.equal(rent.get("responsible").get("id"), responsibleId);
    }

    public static Specification<Rent> placementsIdEquals(Long placementId) {
        return (rent, query, criteriaBuilder) -> criteriaBuilder.equal(rent.get("placement").get("id"), placementId);
    }
}
