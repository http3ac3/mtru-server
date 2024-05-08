package com.vlsu.inventory.repository;

import com.vlsu.inventory.model.Responsible;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ResponsibleRepository extends JpaRepository<Responsible, Long>, JpaSpecificationExecutor<Responsible> {
    @Override
    @EntityGraph(attributePaths = { "department", "user" })
    List<Responsible> findAll(Specification<Responsible> spec);
    @Query("SELECT r FROM Responsible r LEFT JOIN FETCH r.equipment WHERE r.id = ?1")
    Optional<Responsible> findWithEquipmentById(Long id);

    @Override
    @EntityGraph(attributePaths = { "department", "user" })
    Optional<Responsible> findById(Long id);

    List<Responsible> findByLastNameAndFirstNameAndPatronymic(String lastName, String firstName, String patronymic);
    List<Responsible> findByLastNameAndFirstNameAndPatronymicIsNull(String lastName, String firstName);
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
