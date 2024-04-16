package com.vlsu.inventory.repository;

import com.vlsu.inventory.model.Responsible;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ResponsibleRepository extends JpaRepository<Responsible, Long>, JpaSpecificationExecutor<Responsible> {
    @Override
    @EntityGraph(type = EntityGraph.EntityGraphType.FETCH, attributePaths = { "department", "user" })
    List<Responsible> findAll(Specification<Responsible> spec);
    List<Responsible> findByDepartmentId(Long departmentId);
    static Specification<Responsible> isFinanciallyResponsible(Boolean isFinanciallyResponsible) {
        return (responsible, query, criteriaBuilder) -> criteriaBuilder.equal(responsible
                .get("isFinanciallyResponsible"), isFinanciallyResponsible);
    }
    static Specification<Responsible> firstNameLike(String firstName) {
        return (responsible, query, criteriaBuilder) -> criteriaBuilder.like(responsible.get("firstName"), "%"+firstName+"%");
    }
    static Specification<Responsible> lastNameLike(String lastName) {
        return (responsible, query, criteriaBuilder) -> criteriaBuilder.like(responsible.get("lastName"), "%"+lastName+"%");
    }

    static Specification<Responsible> departmentIdEquals(Long departmentId) {
        return (responsible, query, criteriaBuilder) -> criteriaBuilder.equal(responsible.get("department").get("id"), departmentId);
    }
}
