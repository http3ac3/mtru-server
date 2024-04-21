package com.vlsu.inventory.repository;

import com.vlsu.inventory.model.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DepartmentRepository extends JpaRepository<Department, Long> {
    @Query("SELECT d FROM Department d LEFT JOIN FETCH d.responsibles r WHERE d.id = ?1")
    Optional<Department> findWithResponsibleById(Long id);
}
