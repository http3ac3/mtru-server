package com.vlsu.inventory.repository;

import com.vlsu.inventory.model.Responsible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ResponsibleRepository extends JpaRepository<Responsible, Long> {
    @Query("FROM Responsible WHERE isFinanciallyResponsible = ?1")
    List<Responsible> findByFinanciallyResponsibility(boolean isFinanciallyResponsible);

    List<Responsible> findByDepartmentId(Long departmentId);

    @Query("FROM Responsible WHERE firstName = ?1 AND lastName = ?2 AND patronymic = ?3")
    List<Responsible> findByFullName(String firstName, String lastName, String patronymic);
}
