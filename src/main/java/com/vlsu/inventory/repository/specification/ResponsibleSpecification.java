package com.vlsu.inventory.repository.specification;

import com.vlsu.inventory.model.Responsible;
import org.springframework.data.jpa.domain.Specification;

public class ResponsibleSpecification {
    public static Specification<Responsible> isFinanciallyResponsible(Boolean isFinanciallyResponsible) {
        return (responsible, query, criteriaBuilder) -> criteriaBuilder.equal(responsible
                .get("isFinanciallyResponsible"), isFinanciallyResponsible);
    }
    public static Specification<Responsible> firstNameLike(String firstName) {
        return (responsible, query, criteriaBuilder) -> criteriaBuilder.like(responsible.get("firstName"), "%"+firstName+"%");
    }
    public static Specification<Responsible> lastNameLike(String lastName) {
        return (responsible, query, criteriaBuilder) -> criteriaBuilder.like(responsible.get("lastName"), "%"+lastName+"%");
    }

    public static Specification<Responsible> departmentIdEquals(Long departmentId) {
        return (responsible, query, criteriaBuilder) -> criteriaBuilder.equal(responsible.get("department").get("id"), departmentId);
    }
}
