package com.vlsu.inventory.repository;

import com.vlsu.inventory.model.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SubcategoryRepository extends JpaRepository<Subcategory, Long> {
    Optional<Subcategory> findByName(String name);
    List<Subcategory> findByCategoryId(Long categoryId);
}
