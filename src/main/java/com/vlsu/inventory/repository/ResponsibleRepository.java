package com.vlsu.inventory.repository;

import com.vlsu.inventory.model.Responsible;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ResponsibleRepository extends JpaRepository<Responsible, Long> {
    @Query("SELECT * FROM responsible WHERE is_financially_responsible = ?1")
    List<Responsible> findByFinanciallyResponsibility(boolean isFinanciallyResponsible);

    List<Responsible> findByDepartmentId(Long departmentId);

    @Query("SELECT * FROM responsible WHERE first_name = ?1 AND last_name = ?2 AND patronymic = ?3")
    List<Responsible> findByFullName(String firstName, String lastName, String patronymic);
}
