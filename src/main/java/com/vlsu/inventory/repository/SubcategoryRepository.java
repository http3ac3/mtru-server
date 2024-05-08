package com.vlsu.inventory.repository;

import com.vlsu.inventory.model.Subcategory;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {

    @Override
    @EntityGraph(attributePaths = { "category" })
    List<Subcategory> findAll();

    @EntityGraph(attributePaths = { "category" })
    Optional<Subcategory> findById(Long id);

    Optional<Subcategory> findByName(String name);

    @Query("SELECT s FROM Subcategory s LEFT JOIN FETCH s.equipment e WHERE s.id = ?1")
    Optional<Subcategory> findCountOfEquipmentById(Long id);
    List<Subcategory> findByCategoryId(Long categoryId);
}
