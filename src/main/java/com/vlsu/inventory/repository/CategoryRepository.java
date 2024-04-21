package com.vlsu.inventory.repository;

import com.vlsu.inventory.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT c FROM Category c LEFT JOIN FETCH c.subcategories s WHERE c.id = ?1")
    Optional<Category> findWithSubcategoriesById(Long id);
}
