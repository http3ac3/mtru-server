package com.vlsu.inventory.repository;

import com.vlsu.inventory.model.Responsible;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ResponsibleRepository extends JpaRepository<Responsible, Long>, JpaSpecificationExecutor<Responsible> {
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
}
